/*
 * Cinelex
 * SearchViewModel
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieCategory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
	private val repository: MovieRepository,
) : ViewModel() {
	private val _query = MutableStateFlow("")
	val query: StateFlow<String> = _query.asStateFlow()

	/** `null` until a query has been searched: the screen keeps showing recommendations. */
	private val _state = MutableStateFlow<UiState<List<Movie>>?>(null)
	val state: StateFlow<UiState<List<Movie>>?> = _state.asStateFlow()

	private val _isLoadingMore = MutableStateFlow(false)
	val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

	// Recommendations are a cached nicety; Home already surfaces cache failures.
	val recommendations: StateFlow<List<Movie>> =
		repository
			.observeMovies(MovieCategory.POPULAR)
			.catch { if (it !is CinelexException) throw it }
			.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	/** The query behind the current [state], so load-more never mixes queries. */
	private var searchedQuery = ""
	private var page = 1
	private var totalPages = 1
	private var loadMore: Job? = null

	init {
		viewModelScope.launch {
			_query
				.map { it.trim() }
				.debounce { if (it.isEmpty()) 0L else DEBOUNCE_MS }
				.distinctUntilChanged()
				.collectLatest { search(it) }
		}
	}

	fun onQueryChange(query: String) {
		_query.value = query
	}

	fun loadMore() {
		val loaded = _state.value as? UiState.Loaded ?: return
		if (_isLoadingMore.value || page >= totalPages) return
		val query = searchedQuery
		_isLoadingMore.value = true
		loadMore =
			viewModelScope.launch {
				var movies = loaded.data
				try {
					// Keep going until a page adds rows: an unchanged last row never triggers another load.
					while (page < totalPages) {
						val next = page + 1
						val result = repository.searchMovies(query, next)
						page = next
						totalPages = result.totalPages
						// TMDB repeats movies across pages; duplicate keys crash LazyColumn.
						val seen = movies.mapTo(HashSet()) { it.id }
						val fresh = result.movies.filterNot { it.id in seen }
						if (fresh.isNotEmpty()) {
							movies = movies + fresh
							_state.value = UiState.Loaded(movies)
							break
						}
					}
				} catch (_: CinelexException) {
					// Keep what's loaded; the next page is retried when the last row is shown again.
				} finally {
					// A cancelled job was superseded by search(), which already reset the flag.
					if (isActive) _isLoadingMore.value = false
				}
			}
	}

	private suspend fun search(query: String) {
		loadMore?.cancel()
		_isLoadingMore.value = false
		if (query.isEmpty()) {
			_state.value = null
			return
		}
		_state.value = UiState.Loading
		try {
			val result = repository.searchMovies(query, 1)
			searchedQuery = query
			page = 1
			totalPages = result.totalPages
			_state.value = if (result.movies.isEmpty()) UiState.Empty else UiState.Loaded(result.movies)
		} catch (e: CinelexException) {
			_state.value = UiState.Error(e.code)
		}
	}

	private companion object {
		const val DEBOUNCE_MS = 500L
	}
}
