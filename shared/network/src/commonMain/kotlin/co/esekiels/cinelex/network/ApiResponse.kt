/*
 * Cinelex
 * ApiResponse
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.network.model.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_FORBIDDEN = 403
private const val HTTP_TIMEOUT = 408
private const val HTTP_GATEWAY_TIMEOUT = 504
private const val HTTP_SERVER_ERROR_START = 500
private const val HTTP_SERVER_ERROR_END = 599

private const val TIMEOUT_MESSAGE = "The request timed out. Please try again."

sealed interface ApiResponse<out T> {
	data class Success<T>(
		val body: T,
	) : ApiResponse<T>

	data class Failure(
		val error: CinelexException,
	) : ApiResponse<Nothing>
}

inline fun <T, R> ApiResponse<T>.map(transform: (T) -> R): ApiResponse<R> =
	when (this) {
		is ApiResponse.Success -> ApiResponse.Success(transform(body))
		is ApiResponse.Failure -> this
	}

@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(call: suspend () -> T): ApiResponse<T> =
	try {
		ApiResponse.Success(call())
	} catch (e: CancellationException) {
		throw e
	} catch (e: ResponseException) {
		ApiResponse.Failure(e.toCinelexException())
	} catch (e: IOException) {
		ApiResponse.Failure(
			if (e.isTimeout()) {
				CinelexException(ErrorConstants.HTTP_TIMEOUT, e.message ?: TIMEOUT_MESSAGE)
			} else {
				CinelexException(ErrorConstants.NETWORK_ERROR, "Please check your internet connection and try again.")
			},
		)
	} catch (e: Exception) {
		ApiResponse.Failure(
			CinelexException(ErrorConstants.UNKNOWN_ERROR, e.message ?: "Unknown error during network request"),
		)
	}

private fun Throwable.isTimeout(): Boolean =
	this is HttpRequestTimeoutException || this is ConnectTimeoutException || this is SocketTimeoutException

@Suppress("SwallowedException", "TooGenericExceptionCaught")
private suspend fun ResponseException.toCinelexException(): CinelexException {
	val message =
		try {
			response.body<ErrorResponse>().message
		} catch (_: Exception) {
			null
		}
	return when (response.status.value) {
		HTTP_UNAUTHORIZED -> CinelexException(ErrorConstants.HTTP_UNAUTHORIZED, message ?: "Session has expired")
		HTTP_FORBIDDEN ->
			CinelexException(
				ErrorConstants.HTTP_FORBIDDEN,
				"You don't have permission to access this resource.",
			)
		HTTP_TIMEOUT, HTTP_GATEWAY_TIMEOUT -> CinelexException(ErrorConstants.HTTP_TIMEOUT, TIMEOUT_MESSAGE)
		in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END ->
			CinelexException(
				ErrorConstants.UNKNOWN_ERROR,
				"Server error. Please try again later.",
			)
		else -> CinelexException(ErrorConstants.UNKNOWN_ERROR, message ?: "An unexpected error occurred.")
	}
}
