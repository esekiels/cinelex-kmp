/*
 * Cinelex
 * DetailViewModel
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.model.MovieDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
	private val repository: MovieRepository,
	private val movieId: Int,
) : ViewModel() {
	private val _state = MutableStateFlow<UiState<MovieDetails>>(UiState.Loading)
	val state: StateFlow<UiState<MovieDetails>> = _state.asStateFlow()

	init {
		viewModelScope.launch {
			_state.value =
				try {
					UiState.Loaded(repository.fetchMovieDetails(movieId))
				} catch (e: CinelexException) {
					UiState.Error(e.code, e.message)
				}
		}
	}
}
