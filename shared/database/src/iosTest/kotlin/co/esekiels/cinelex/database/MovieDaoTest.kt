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
	) = MovieEntity(
		id = id,
		title = title,
		backdropPath = "/b$id.jpg",
		posterPath = "/p$id.jpg",
		category = category,
	)

	@Test
	fun sameMovieCanLiveInTwoCategories() =
		runTest {
			dao.saveMovies(listOf(movie(1, "popular"), movie(1, "top_rated")))

			assertEquals(1, dao.fetchByCategory("popular").size)
			assertEquals(1, dao.fetchByCategory("top_rated").size)
		}

	@Test
	fun replaceCategoryDropsStaleRows() =
		runTest {
			dao.saveMovies(listOf(movie(1, "popular"), movie(2, "popular")))

			dao.replaceCategory("popular", listOf(movie(3, "popular")))

			val remaining = dao.fetchByCategory("popular")
			assertEquals(listOf(3), remaining.map { it.id })
		}
}
