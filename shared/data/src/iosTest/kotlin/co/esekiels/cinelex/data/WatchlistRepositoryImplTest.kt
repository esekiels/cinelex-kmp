/*
 * Cinelex
 * WatchlistRepositoryImplTest
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WatchlistRepositoryImplTest {
	private val harness = Harness()

	@AfterTest
	fun tearDown() = harness.close()

	private fun TestScope.repository() =
		WatchlistRepositoryImpl(
			dao = harness.database.watchlistDao(),
			ioDispatcher = UnconfinedTestDispatcher(testScheduler),
		)

	private val shawshank =
		Movie(
			id = 278,
			title = "The Shawshank Redemption",
			posterPath = "/p.jpg",
			releaseDate = "1994-09-23",
			voteAverage = 8.7,
		)

	@Test
	fun addedMovieIsListedWithItsDetails() =
		runTest {
			val repository = repository()

			repository.add(shawshank)

			assertEquals(listOf(shawshank), repository.observeWatchlist().first())
			assertTrue(harness.requests.isEmpty(), "the watchlist must never hit the network")
		}

	@Test
	fun addAndRemoveToggleMembership() =
		runTest {
			val repository = repository()

			repository.add(shawshank)
			assertTrue(repository.observeIsInWatchlist(278).first())
			repository.remove(278)
			assertFalse(repository.observeIsInWatchlist(278).first())
			assertTrue(repository.observeWatchlist().first().isEmpty())
		}

	@Test
	fun openListUpdatesOnAddAndRemove() =
		runTest {
			val repository = repository()
			val emissions = repository.observeWatchlist().produceIn(backgroundScope)
			assertEquals(emptyList(), emissions.receive())

			repository.add(shawshank)
			assertEquals(listOf(shawshank), emissions.receive())

			repository.remove(278)
			assertEquals(emptyList(), emissions.receive())
		}
}
