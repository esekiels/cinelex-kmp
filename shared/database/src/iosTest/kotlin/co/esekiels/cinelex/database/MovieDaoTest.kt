/*
 * Cinelex
 * MovieDaoTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import co.esekiels.cinelex.database.entity.MovieEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDaoTest {
	private val database = testDatabase()
	private val dao = database.movieDao()

	@AfterTest
	fun tearDown() = database.close()

	private fun movie(
		id: Int,
		category: String,
		title: String = "M$id",
		position: Int = 0,
		language: String = "en",
	) = MovieEntity(
		id = id,
		title = title,
		backdropPath = "/b$id.jpg",
		posterPath = "/p$id.jpg",
		category = category,
		position = position,
		language = language,
	)

	@Test
	fun categoryKeepsSavedPositionOrder() =
		runTest {
			dao.saveMovies(
				listOf(
					movie(1, "popular", position = 2),
					movie(2, "popular", position = 0),
					movie(3, "popular", position = 1),
				),
			)

			assertEquals(listOf(2, 3, 1), dao.fetchByCategory("popular", "en").map { it.id })
		}

	@Test
	fun sameMovieCanLiveInTwoCategories() =
		runTest {
			dao.saveMovies(listOf(movie(1, "popular"), movie(1, "top_rated")))

			assertEquals(1, dao.fetchByCategory("popular", "en").size)
			assertEquals(1, dao.fetchByCategory("top_rated", "en").size)
		}

	@Test
	fun replaceCategoryDropsStaleRows() =
		runTest {
			dao.saveMovies(listOf(movie(1, "popular"), movie(2, "popular")))

			dao.replaceCategory("popular", "en", listOf(movie(3, "popular")))

			val remaining = dao.fetchByCategory("popular", "en")
			assertEquals(listOf(3), remaining.map { it.id })
		}

	@Test
	fun languagesAreCachedSeparately() =
		runTest {
			dao.saveMovies(listOf(movie(1, "popular", language = "en"), movie(2, "popular", language = "id")))

			dao.replaceCategory("popular", "id", listOf(movie(3, "popular", language = "id")))

			assertEquals(listOf(1), dao.fetchByCategory("popular", "en").map { it.id })
			assertEquals(listOf(3), dao.fetchByCategory("popular", "id").map { it.id })
		}
}
