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
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.testing.FakeMovieRepository
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
class DetailViewModelTest {
	private val movieId = MovieStubs.all.first().id

	@BeforeTest
	fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

	@AfterTest
	fun tearDown() = Dispatchers.resetMain()

	@Test
	fun loadedWithDetails() {
		val state = DetailViewModel(FakeMovieRepository(), movieId).state.value
		assertEquals(UiState.Loaded(MovieStubs.details(movieId)), state)
	}

	@Test
	fun errorWhenDetailsFail() {
		val failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Server unreachable")
		val repository = FakeMovieRepository().apply { detailsFailure = failure }

		assertEquals(UiState.Error(failure.code, failure.message), DetailViewModel(repository, movieId).state.value)
	}

	@Test
	fun retryRecoversAfterFailure() {
		val offline = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")
		val repository = FakeMovieRepository().apply { detailsFailure = offline }
		val viewModel = DetailViewModel(repository, movieId)

		repository.detailsFailure = null
		viewModel.load()

		assertEquals(UiState.Loaded(MovieStubs.details(movieId)), viewModel.state.value)
	}

	@Test
	fun keepsCachedDetailsWhenRefreshFails() =
		runTest {
			val repository = FakeMovieRepository().apply { refreshMovieDetails(movieId) }
			repository.detailsFailure = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")

			assertEquals(UiState.Loaded(MovieStubs.details(movieId)), DetailViewModel(repository, movieId).state.value)
		}

	@Test
	fun unknownErrorMapsToUnknownCode() {
		val repository =
			object : MovieRepository by FakeMovieRepository() {
				override suspend fun refreshMovieDetails(id: Int) = error("Disk full")
			}

		val state = DetailViewModel(repository, movieId).state.value

		assertEquals(UiState.Error(ErrorConstants.UNKNOWN_ERROR, "Disk full"), state)
	}
}
