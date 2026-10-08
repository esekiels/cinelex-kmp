/*
 * Cinelex
 * WatchlistViewModel
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.feature.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.data.WatchlistRepository
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WatchlistViewModel(
	repository: WatchlistRepository,
) : ViewModel() {
	val state: StateFlow<UiState<List<Movie>>> =
		repository
			.observeWatchlist()
			.map { if (it.isEmpty()) UiState.Empty else UiState.Loaded(it) }
			.catch { if (it is CinelexException) emit(UiState.Error(it.code)) else throw it }
			.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)
}
