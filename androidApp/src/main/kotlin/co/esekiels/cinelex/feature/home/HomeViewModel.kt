/*
 * Cinelex
 * HomeViewModel
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class Carousels(
	val nowPlaying: List<Movie>,
	val popular: List<Movie>,
	val upcoming: List<Movie>,
	val topRated: List<Movie>,
) {
	val isEmpty: Boolean
		get() = nowPlaying.isEmpty() && popular.isEmpty() && upcoming.isEmpty() && topRated.isEmpty()
}

class HomeViewModel(
	private val repository: MovieRepository,
) : ViewModel() {
	private val _state = MutableStateFlow<UiState<Carousels>>(UiState.Loading)
	val state: StateFlow<UiState<Carousels>> = _state.asStateFlow()

	private val _isRefreshing = MutableStateFlow(false)
	val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

	init {
		carousels()
			.onEach { if (!it.isEmpty) _state.value = UiState.Loaded(it) }
			.catch { if (it is CinelexException) fail(it) else throw it }
			.launchIn(viewModelScope)
		viewModelScope.launch { load() }
	}

	fun refresh() {
		viewModelScope.launch {
			_isRefreshing.value = true
			try {
				load()
			} finally {
				_isRefreshing.value = false
			}
		}
	}

	private suspend fun load() {
		try {
			repository.refreshMovies()
			if (_state.value !is UiState.Loaded && carousels().first().isEmpty) _state.value = UiState.Empty
		} catch (e: CinelexException) {
			fail(e)
		}
	}

	private fun carousels(): Flow<Carousels> =
		combine(
			repository.observeNowPlaying(),
			repository.observePopular(),
			repository.observeUpcoming(),
			repository.observeTopRated(),
			::Carousels,
		)

	private fun fail(e: CinelexException) {
		if (_state.value !is UiState.Loaded) _state.value = UiState.Error(e.code, e.message)
	}
}
