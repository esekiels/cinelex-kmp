/*
 * Cinelex
 * Guarded
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/** Runs [block] on this dispatcher so every failure reaches callers as a [CinelexException]. */
@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> CoroutineDispatcher.guarded(block: suspend () -> T): T =
	try {
		withContext(this) { block() }
	} catch (e: CancellationException) {
		throw e
	} catch (e: Exception) {
		throw e.toCinelexException()
	}

internal fun Throwable.toCinelexException(): CinelexException =
	this as? CinelexException
		?: CinelexException(ErrorConstants.UNKNOWN_ERROR, message ?: "An unexpected error occurred.", this)
