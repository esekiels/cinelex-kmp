/*
 * Cinelex
 * UiState
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.core.common

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants

sealed interface UiState<out T> {
	data object Loading : UiState<Nothing>

	data class Loaded<T>(
		val data: T,
	) : UiState<T>

	data object Empty : UiState<Nothing>

	data class Error(
		val code: String,
		val message: String,
	) : UiState<Nothing>
}

fun Throwable.toUiError(): UiState.Error =
	if (this is CinelexException) {
		UiState.Error(code, message)
	} else {
		UiState.Error(ErrorConstants.UNKNOWN_ERROR, message.orEmpty())
	}
