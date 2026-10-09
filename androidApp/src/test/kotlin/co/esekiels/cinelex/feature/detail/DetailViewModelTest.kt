/*
 * Cinelex
 * DetailViewModelTest
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.feature.detail

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.testing.FakeMovieRepository
import co.esekiels.cinelex.testing.FakeWatchlistRepository
import co.esekiels.cinelex.testing.MovieStubs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DetailViewModelTest {
	private val movieId = MovieStubs.all.first().id

	private val watchlist = FakeWatchlistRepository()

	private fun detail(movies: FakeMovieRepository = FakeMovieRepository()) = DetailViewModel(movies, watchlist, movieId)

	@BeforeTest
	fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

	@AfterTest
	fun tearDown() = Dispatchers.resetMain()

	@Test
	fun loadedWithDetails() {
		val state = detail().state.value
		assertEquals(UiState.Loaded(MovieStubs.details(movieId)), state)
	}

	@Test
	fun errorWhenDetailsFail() {
		val failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Server unreachable")
		val repository = FakeMovieRepository().apply { detailsFailure = failure }

		assertEquals(UiState.Error(failure.code), detail(repository).state.value)
	}

	@Test
	fun retryRecoversAfterFailure() {
		val offline = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")
		val repository = FakeMovieRepository().apply { detailsFailure = offline }
		val viewModel = detail(repository)

		repository.detailsFailure = null
		viewModel.load()

		assertEquals(UiState.Loaded(MovieStubs.details(movieId)), viewModel.state.value)
	}

	@Test
	fun keepsCachedDetailsWhenRefreshFails() =
		runTest {
			val repository = FakeMovieRepository().apply { refreshMovieDetails(movieId) }
			repository.detailsFailure = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")

			assertEquals(UiState.Loaded(MovieStubs.details(movieId)), detail(repository).state.value)
		}

	@Test
	fun toggleSavesTheLoadedMovieThenRemovesIt() {
		val viewModel = detail()

		viewModel.toggleWatchlist()
		assertTrue(viewModel.isSaved.value)
		assertEquals(listOf(MovieStubs.details(movieId).toMovie()), watchlist.movies.value)

		viewModel.toggleWatchlist()
		assertFalse(viewModel.isSaved.value)
		assertTrue(watchlist.movies.value.isEmpty())
	}

	@Test
	fun alreadySavedMovieOpensAsSaved() =
		runTest {
			watchlist.add(MovieStubs.details(movieId).toMovie())

			assertTrue(detail().isSaved.value)
		}

	@Test
	fun toggleDoesNothingUntilDetailsLoad() {
		val offline = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")
		val repository = FakeMovieRepository().apply { detailsFailure = offline }
		val viewModel = detail(repository)

		viewModel.toggleWatchlist()

		assertFalse(viewModel.isSaved.value)
	}
}
