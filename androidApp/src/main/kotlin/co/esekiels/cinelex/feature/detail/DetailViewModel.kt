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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DetailViewModel(
	private val repository: MovieRepository,
	private val movieId: Int,
) : ViewModel() {
	private val _state = MutableStateFlow<UiState<MovieDetails>>(UiState.Loading)
	val state: StateFlow<UiState<MovieDetails>> = _state.asStateFlow()

	private var refresh: Job? = null

	init {
		repository
			.observeMovieDetails(movieId)
			.filterNotNull()
			.onEach { _state.value = UiState.Loaded(it) }
			.catch { if (it is CinelexException) fail(it) else throw it }
			.launchIn(viewModelScope)
		load()
	}

	fun load() {
		if (refresh?.isActive == true) return
		if (_state.value is UiState.Error) _state.value = UiState.Loading
		refresh =
			viewModelScope.launch {
				try {
					repository.refreshMovieDetails(movieId)
				} catch (e: CinelexException) {
					fail(e)
				}
			}
	}

	private fun fail(e: CinelexException) {
		if (_state.value !is UiState.Loaded) _state.value = UiState.Error(e.code, e.message)
	}
}
