/*
 * Cinelex
 * MovieRepositoryImplTest
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.entity.MovieDetailsEntity
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.network.ApiConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {
	private val harness = Harness()

	@AfterTest
	fun tearDown() = harness.close()

	private val language = MutableStateFlow(Language.ENGLISH)

	private fun TestScope.repository(
		dao: MovieDao = harness.dao,
		language: Flow<Language> = this@MovieRepositoryImplTest.language,
	) = MovieRepositoryImpl(
		client = harness.movieClient,
		dao = dao,
		language = language,
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
				assertTrue(url.contains("language=en"), "no language in $url")
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

	@Test
	fun refreshDetailsFeedsTheObservedCache() =
		runTest {
			val repository = repository()
			assertNull(repository.observeMovieDetails(278).first())

			harness.alwaysRespond("""{ "id": 278, "title": "The Shawshank Redemption", "runtime": 142 }""")
			repository.refreshMovieDetails(278)

			assertEquals(142, repository.observeMovieDetails(278).first()?.runtime)
			assertTrue(harness.requests.single().contains("movie/278"))
		}

	@Test
	fun refreshDetailsOfflineThrowsAndKeepsTheCache() =
		runTest {
			harness.alwaysRespond("""{ "id": 278, "title": "The Shawshank Redemption" }""")
			val repository = repository()
			repository.refreshMovieDetails(278)

			harness.alwaysOffline()
			val error = assertFailsWith<CinelexException> { repository.refreshMovieDetails(278) }

			assertEquals(ErrorConstants.NETWORK_ERROR, error.code)
			assertEquals("The Shawshank Redemption", repository.observeMovieDetails(278).first()?.title)
		}

	@Test
	fun eachLanguageHasItsOwnCache() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()
			repository.refreshMovies()

			language.value = Language.INDONESIAN

			assertTrue(repository.observeNowPlaying().first().isEmpty())
			repository.refreshMovies()
			assertTrue(harness.requests.last().contains("language=id"))
			assertEquals(1, repository.observeNowPlaying().first().size)
		}

	@Test
	fun hasCachedMoviesChecksTheCurrentLanguage() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			val repository = repository()
			assertFalse(repository.hasCachedMovies())

			repository.refreshMovies()
			assertTrue(repository.hasCachedMovies())

			language.value = Language.INDONESIAN
			assertFalse(repository.hasCachedMovies())
		}

	@Test
	fun contentLanguageEmitsOnlyRealChanges() =
		runTest {
			val preferences = flowOf(Language.ENGLISH, Language.ENGLISH, Language.INDONESIAN)

			val emitted = repository(language = preferences).observeContentLanguage().toList()

			assertEquals(listOf(Language.ENGLISH, Language.INDONESIAN), emitted)
		}

	@Test
	fun detailsFollowTheLanguage() =
		runTest {
			harness.alwaysRespond("""{ "id": 278, "title": "Penebusan Shawshank" }""")
			language.value = Language.INDONESIAN
			val repository = repository()

			repository.refreshMovieDetails(278)

			assertTrue(harness.requests.single().contains("language=id"))
			assertEquals("Penebusan Shawshank", repository.observeMovieDetails(278).first()?.title)
			language.value = Language.ENGLISH
			assertNull(repository.observeMovieDetails(278).first())
		}

	@Test
	fun observeDetailsTreatsAnUnreadableRowAsMissing() =
		runTest {
			harness.dao.saveDetails(MovieDetailsEntity(id = 278, language = "en", json = "not json"))

			assertNull(repository().observeMovieDetails(278).first())
		}

	@Test
	fun databaseFailureSurfacesAsUnknownCinelexException() =
		runTest {
			harness.alwaysRespond("""{ "id": 278, "title": "The Shawshank Redemption" }""")
			val failingDao =
				object : MovieDao by harness.dao {
					override suspend fun saveDetails(details: MovieDetailsEntity) = error("disk full")
				}

			val error = assertFailsWith<CinelexException> { repository(failingDao).refreshMovieDetails(278) }

			assertEquals(ErrorConstants.UNKNOWN_ERROR, error.code)
		}

	@Test
	fun searchHitsTheNetworkInTheCurrentLanguageAndCachesNothing() =
		runTest {
			harness.alwaysRespond(MOVIE_PAGE)
			language.value = Language.INDONESIAN
			val repository = repository()

			val result = repository.searchMovies("shawshank", page = 1)

			assertEquals(listOf(278), result.movies.map { it.id })
			val url = harness.requests.single()
			assertTrue(url.contains(ApiConstants.SEARCH) && url.contains("query=shawshank"), url)
			assertTrue(url.contains("language=id"), url)
			assertFalse(repository.hasCachedMovies(), "search results must not land in the carousel cache")
		}

	@Test
	fun searchOfflineThrowsNetworkError() =
		runTest {
			harness.alwaysOffline()

			val error = assertFailsWith<CinelexException> { repository().searchMovies("shawshank", page = 1) }

			assertEquals(ErrorConstants.NETWORK_ERROR, error.code)
		}

	@Test
	fun searchRequestsThePageAndReportsTotalPages() =
		runTest {
			harness.alwaysRespond("""{ "page": 2, "total_pages": 5, "results": [{ "id": 238, "title": "The Godfather" }] }""")

			val result = repository().searchMovies("god", page = 2)

			assertEquals(5, result.totalPages)
			assertEquals(listOf(238), result.movies.map { it.id })
			assertTrue(harness.requests.single().contains("page=2"))
		}
}
