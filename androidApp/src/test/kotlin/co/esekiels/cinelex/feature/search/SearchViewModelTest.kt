/*
 * Cinelex
 * SearchViewModelTest
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.feature.search

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.SearchResult
import co.esekiels.cinelex.testing.FakeMovieRepository
import co.esekiels.cinelex.testing.MovieStubs
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class SearchViewModelTest {
	private lateinit var dispatcher: TestDispatcher

	@BeforeTest
	fun setUp() {
		dispatcher = UnconfinedTestDispatcher()
		Dispatchers.setMain(dispatcher)
	}

	@AfterTest
	fun tearDown() = Dispatchers.resetMain()

	private fun SearchViewModel.search(query: String) {
		onQueryChange(query)
		dispatcher.scheduler.advanceUntilIdle()
	}

	@Test
	fun showsRecommendationsBeforeAnySearch() {
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all))

		assertEquals(MovieStubs.all, viewModel.recommendations.value)
		assertNull(viewModel.state.value)
	}

	@Test
	fun searchesOnlyAfterTheDebounce() {
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all))

		viewModel.onQueryChange("god")
		dispatcher.scheduler.advanceTimeBy(499)
		assertNull(viewModel.state.value, "searched before the debounce elapsed")

		dispatcher.scheduler.advanceUntilIdle()
		assertEquals(UiState.Loaded(listOf(MovieStubs.godfather)), viewModel.state.value)
	}

	@Test
	fun noMatchIsEmpty() {
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all))

		viewModel.search("zzz")

		assertEquals(UiState.Empty, viewModel.state.value)
	}

	@Test
	fun failureIsError() {
		val failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Server unreachable")
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all).apply { this.failure = failure })

		viewModel.search("god")

		assertEquals(UiState.Error(failure.code), viewModel.state.value)
	}

	@Test
	fun clearingTheQueryReturnsToRecommendations() {
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all))
		viewModel.search("god")

		viewModel.search("")

		assertNull(viewModel.state.value)
	}

	@Test
	fun loadMoreAppendsTheNextPageUntilTheLast() {
		val viewModel = SearchViewModel(FakeMovieRepository(MovieStubs.all))
		viewModel.search("the")
		assertEquals(UiState.Loaded(listOf(MovieStubs.shawshank)), viewModel.state.value)

		viewModel.loadMore()
		viewModel.loadMore()

		assertEquals(UiState.Loaded(MovieStubs.all), viewModel.state.value)
		assertFalse(viewModel.isLoadingMore.value)
	}

	@Test
	fun loadMoreSkipsRepeatedMoviesAndKeepsPagingPastAPageOfThem() {
		val pages = listOf(listOf(MovieStubs.shawshank), listOf(MovieStubs.shawshank), listOf(MovieStubs.godfather))
		val requested = mutableListOf<Int>()
		val repository =
			object : MovieRepository by FakeMovieRepository() {
				override suspend fun searchMovies(
					query: String,
					page: Int,
				): SearchResult {
					requested += page
					return SearchResult(pages[page - 1], totalPages = pages.size)
				}
			}
		val viewModel = SearchViewModel(repository)
		viewModel.search("the")

		viewModel.loadMore()

		assertEquals(UiState.Loaded(MovieStubs.all), viewModel.state.value)
		assertEquals(listOf(1, 2, 3), requested)
	}

	@Test
	fun loadMoreFailureKeepsLoadedResults() {
		val repository = FakeMovieRepository(MovieStubs.all)
		val viewModel = SearchViewModel(repository)
		viewModel.search("the")

		repository.failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Offline")
		viewModel.loadMore()

		assertEquals(UiState.Loaded(listOf(MovieStubs.shawshank)), viewModel.state.value)
		assertFalse(viewModel.isLoadingMore.value)
	}

	@Test
	fun newSearchDiscardsAPageStillLoadingForTheOldQuery() {
		val slowPage = CompletableDeferred<SearchResult>()
		val fake = FakeMovieRepository(MovieStubs.all)
		val repository =
			object : MovieRepository by fake {
				override suspend fun searchMovies(
					query: String,
					page: Int,
				): SearchResult = if (query == "the" && page == 2) slowPage.await() else fake.searchMovies(query, page)
			}
		val viewModel = SearchViewModel(repository)
		viewModel.search("the")
		viewModel.loadMore()

		viewModel.search("god")
		// A movie not yet shown, so an uncancelled stale page would visibly append.
		slowPage.complete(SearchResult(listOf(MovieStubs.godfather.copy(id = 1)), totalPages = 2))
		dispatcher.scheduler.advanceUntilIdle()

		assertEquals(UiState.Loaded(listOf(MovieStubs.godfather)), viewModel.state.value)
		assertFalse(viewModel.isLoadingMore.value)
	}
}
