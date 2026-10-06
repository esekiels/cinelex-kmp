/*
 * Cinelex
 * WatchlistViewModelTest
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.feature.watchlist

import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.testing.FakeWatchlistRepository
import co.esekiels.cinelex.testing.MovieStubs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {
	@BeforeTest
	fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

	@AfterTest
	fun tearDown() = Dispatchers.resetMain()

	@Test
	fun emptyWhenNothingIsSaved() {
		assertEquals(UiState.Empty, WatchlistViewModel(FakeWatchlistRepository()).state.value)
	}

	@Test
	fun followsTheSavedMovies() =
		runTest {
			val repository = FakeWatchlistRepository()
			val viewModel = WatchlistViewModel(repository)
			val movie = MovieStubs.all.first()

			repository.add(movie)
			assertEquals(UiState.Loaded(listOf(movie)), viewModel.state.value)

			repository.remove(movie.id)
			assertEquals(UiState.Empty, viewModel.state.value)
		}
}
