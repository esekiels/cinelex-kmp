/*
 * Cinelex
 * MapperTest
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.database

import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toEntities
import co.esekiels.cinelex.model.Movie
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {
	@Test
	fun movieMapperSubstitutesEmptyStringForNullPaths() {
		val entities = listOf(Movie(id = 1, title = "X")).toEntities("popular")

		assertEquals("", entities.single().posterPath)
		assertEquals("popular", entities.single().category)
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
