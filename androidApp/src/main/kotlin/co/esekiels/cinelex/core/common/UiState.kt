/*
 * Cinelex
 * UiState
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.core.common

sealed interface UiState<out T> {
	data object Loading : UiState<Nothing>

	data class Loaded<T>(
		val data: T,
	) : UiState<T>

	data object Empty : UiState<Nothing>

	data class Error(
		val code: String,
	) : UiState<Nothing>
}
