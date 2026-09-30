/*
 * Cinelex
 * MapperTest
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.database

import co.esekiels.cinelex.database.entity.MovieDetailsEntity
import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toDomainOrNull
import co.esekiels.cinelex.database.entity.mapper.toEntities
import co.esekiels.cinelex.database.entity.mapper.toEntity
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapperTest {
	@Test
	fun movieMapperSubstitutesEmptyStringForNullPaths() {
		val entities = listOf(Movie(id = 1, title = "X")).toEntities("popular")

		assertEquals("", entities.single().posterPath)
		assertEquals("popular", entities.single().category)
	}

	@Test
	fun movieMapperRestoresNullPaths() {
		val movie = listOf(Movie(id = 1, title = "X")).toEntities("popular").toDomain().single()

		assertNull(movie.posterPath)
		assertNull(movie.posterUrl)
		assertNull(movie.backdropUrl)
	}

	@Test
	fun movieMapperKeepsListOrderAsPosition() {
		val entities = listOf(Movie(id = 9, title = "A"), Movie(id = 3, title = "B")).toEntities("popular")

		assertEquals(listOf(0, 1), entities.map { it.position })
	}

	@Test
	fun detailsRoundTripThroughCache() {
		val details = MovieDetails(id = 278, title = "The Shawshank Redemption", runtime = 142)

		assertEquals(details, details.toEntity().toDomainOrNull())
	}

	@Test
	fun unreadableDetailsCacheIsAMiss() {
		assertNull(MovieDetailsEntity(id = 278, json = "{\"id\": 278}").toDomainOrNull())
		assertNull(MovieDetailsEntity(id = 278, json = "not json").toDomainOrNull())
	}

	@Test
	fun movieMapperIsLossyByDesign() {
		val original = Movie(id = 1, title = "X", voteAverage = 8.7, releaseDate = "1994-01-01")

		val roundTripped =
			original
				.let { listOf(it) }
				.toEntities("popular")
				.toDomain()
				.single()

		// Only what a carousel cell renders survives the round trip.
		assertEquals("X", roundTripped.title)
		assertEquals("0.0", roundTripped.rating)
		assertEquals("", roundTripped.releaseDate)
	}
}
