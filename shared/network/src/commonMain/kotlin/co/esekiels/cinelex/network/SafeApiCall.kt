/*
 * Cinelex
 * SafeApiCall
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
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_FORBIDDEN = 403
private const val HTTP_TIMEOUT = 408
private const val HTTP_GATEWAY_TIMEOUT = 504
private const val HTTP_SERVER_ERROR_START = 500
private const val HTTP_SERVER_ERROR_END = 599

private const val TIMEOUT_MESSAGE = "The request timed out. Please try again."
private const val UNEXPECTED_MESSAGE = "An unexpected error occurred."

suspend fun <T> safeApiCall(call: suspend () -> T): T =
	try {
		call()
	} catch (e: CancellationException) {
		throw e
	} catch (e: Exception) {
		throw e.asCinelexException()
	}

private suspend fun Exception.asCinelexException(): CinelexException =
	when (this) {
		is ResponseException -> toCinelexException()
		is IOException ->
			if (isTimeout()) {
				CinelexException(ErrorConstants.HTTP_TIMEOUT, message ?: TIMEOUT_MESSAGE, this)
			} else {
				CinelexException(ErrorConstants.NETWORK_ERROR, "Please check your internet connection and try again.", this)
			}
		is ContentConvertException, is SerializationException ->
			CinelexException(ErrorConstants.UNKNOWN_ERROR, UNEXPECTED_MESSAGE, this)
		else -> CinelexException(ErrorConstants.UNKNOWN_ERROR, message ?: "Unknown error during network request", this)
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
		else -> CinelexException(ErrorConstants.UNKNOWN_ERROR, message ?: UNEXPECTED_MESSAGE)
	}
}
