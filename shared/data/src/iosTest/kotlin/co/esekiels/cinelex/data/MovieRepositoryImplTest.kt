/*
 * Cinelex
 * MovieRepositoryImplTest
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.network.ApiConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {
	private val harness = Harness()

	@AfterTest
	fun tearDown() = harness.close()

	private fun TestScope.repository() =
		MovieRepositoryImpl(
			client = harness.movieClient,
			dao = harness.dao,
			ioDispatcher = UnconfinedTestDispatcher(testScheduler),
		)

	@Test
	fun refreshFillsEveryCarousel() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()

			repository.refreshMovies()

			assertEquals(1, repository.observeNowPlaying().first().size)
			assertEquals(1, repository.observeUpcoming().first().size)
			assertEquals(1, repository.observeTopRated().first().size)
			assertEquals(1, repository.observePopular().first().size)
			assertEquals(MOVIE_CATEGORIES.size, harness.requests.size)
		}

	@Test
	fun observeNeverHitsTheNetwork() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()

			// Reads are database-only; only refreshMovies() is allowed to fetch.
			repeat(3) { repository.observeNowPlaying().first() }

			assertTrue(harness.requests.isEmpty(), "reads leaked to the network: ${harness.requests}")
		}

	@Test
	fun everyRequestCarriesTheLanguage() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)

			repository().refreshMovies()

			for (category in MOVIE_CATEGORIES) {
				val url = harness.requests.single { it.contains(category) }
				assertTrue(url.contains("language=en-us"), "no language in $url")
				assertTrue(url.contains("page=1"), "no page in $url")
			}
		}

	@Test
	fun refreshReplacesRatherThanAccumulates() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()
			repository.refreshMovies()

			harness.alwaysRespond(MOVIE_PAGE_UPDATED)
			repository.refreshMovies()

			val popular = repository.observePopular().first()
			assertEquals(1, popular.size)
			assertEquals(999, popular.single().id)
		}

	@Test
	fun offlineWithColdCacheThrowsNetworkError() =
		runTest {
			harness.alwaysOffline()

			val error = assertFailsWith<CinelexException> { repository().refreshMovies() }

			assertEquals(ErrorConstants.NETWORK_ERROR, error.code)
		}

	@Test
	fun offlineWithWarmCacheStillThrowsButKeepsRows() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()
			repository.refreshMovies()

			harness.alwaysOffline()

			assertFailsWith<CinelexException> { repository.refreshMovies() }
			assertEquals(1, repository.observeNowPlaying().first().size)
		}

	@Test
	fun failureOnTheFirstCategoryLeavesTheRestUntouched() =
		runTest {
			harness.alwaysOffline()

			assertFailsWith<CinelexException> { repository().refreshMovies() }
			assertEquals(1, harness.requests.size)
			assertTrue(harness.requests.single().contains(ApiConstants.NOW_PLAYING))
		}
}
