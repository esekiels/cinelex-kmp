/*
 * Cinelex
 * HomeViewModelTest
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.feature.home

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.testing.FakeMovieRepository
import co.esekiels.cinelex.testing.MovieStubs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
	@BeforeTest
	fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

	@AfterTest
	fun tearDown() = Dispatchers.resetMain()

	@Test
	fun loadedWhenCarouselsHaveMovies() {
		val state = HomeViewModel(FakeMovieRepository(MovieStubs.all)).state.value
		assertEquals(MovieStubs.all, assertIs<UiState.Loaded<Carousels>>(state).data.nowPlaying)
	}

	@Test
	fun loadingWhileNothingArrives() {
		assertEquals(UiState.Loading, HomeViewModel(FakeMovieRepository(emptyList(), isLoading = true)).state.value)
	}

	@Test
	fun initialLoadDoesNotShowPullIndicator() {
		val viewModel = HomeViewModel(FakeMovieRepository(MovieStubs.all, isLoading = true))
		assertEquals(false, viewModel.isRefreshing.value)
		viewModel.refresh()
		assertEquals(true, viewModel.isRefreshing.value)
	}

	@Test
	fun emptyWhenRefreshSucceedsWithNoMovies() {
		assertEquals(UiState.Empty, HomeViewModel(FakeMovieRepository(emptyList())).state.value)
	}

	@Test
	fun errorOnlyWhenNothingCached() {
		val failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Server unreachable")
		val empty = FakeMovieRepository(emptyList()).apply { this.failure = failure }
		val cached = FakeMovieRepository(MovieStubs.all).apply { this.failure = failure }

		assertEquals(UiState.Error(failure.code), HomeViewModel(empty).state.value)
		assertIs<UiState.Loaded<Carousels>>(HomeViewModel(cached).state.value)
	}

	@Test
	fun refreshesWhenContentLanguageChanges() {
		val repository = FakeMovieRepository(MovieStubs.all)
		HomeViewModel(repository)
		assertEquals(1, repository.refreshCount)

		repository.contentLanguage.value = Language.INDONESIAN

		assertEquals(2, repository.refreshCount)
	}
}
