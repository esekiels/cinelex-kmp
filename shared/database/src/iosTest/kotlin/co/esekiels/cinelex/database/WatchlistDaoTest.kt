/*
 * Cinelex
 * WatchlistDaoTest
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.database

import co.esekiels.cinelex.database.entity.WatchlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WatchlistDaoTest {
	private val database = testDatabase()
	private val dao = database.watchlistDao()

	@AfterTest
	fun tearDown() = database.close()

	private fun saved(
		id: Int,
		addedAt: Long,
	) = WatchlistEntity(id, "M$id", posterPath = null, backdropPath = "/b$id.jpg", "2001-01-01", 7.5, addedAt)

	@Test
	fun newestSavedComesFirst() =
		runTest {
			dao.save(saved(1, addedAt = 10))
			dao.save(saved(2, addedAt = 20))

			assertEquals(listOf(2, 1), dao.observeAll().first().map { it.id })
		}

	@Test
	fun savedRowRoundTrips() =
		runTest {
			dao.save(saved(1, addedAt = 10))

			assertEquals(listOf(saved(1, addedAt = 10)), dao.observeAll().first())
		}

	@Test
	fun isSavedFollowsSaveAndDelete() =
		runTest {
			assertFalse(dao.observeIsSaved(1).first())
			dao.save(saved(1, addedAt = 1))
			assertTrue(dao.observeIsSaved(1).first())
			dao.delete(1)
			assertFalse(dao.observeIsSaved(1).first())
		}

	@Test
	fun savingAgainKeepsTheOriginalPosition() =
		runTest {
			dao.save(saved(1, addedAt = 10))
			dao.save(saved(2, addedAt = 20))
			dao.save(saved(1, addedAt = 30))

			assertEquals(listOf(2, 1), dao.observeAll().first().map { it.id })
		}
}
