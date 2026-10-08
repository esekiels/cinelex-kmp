/*
 * Cinelex
 * MovieTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieTest {
	private val json =
		Json {
			ignoreUnknownKeys = true
			coerceInputValues = true
		}

	@Test
	fun decodesTmdbSnakeCaseAndBuildsUrls() {
		val decoded =
			json.decodeFromString<Movie>(
				"""
				{
				  "id": 278,
				  "title": "The Shawshank Redemption",
				  "poster_path": "/poster.jpg",
				  "backdrop_path": null,
				  "release_date": "1994-09-23",
				  "vote_average": 8.71,
				  "vote_count": 28000,
				  "genre_ids": [18, 80]
				}
				""".trimIndent(),
			)

		assertEquals(278, decoded.id)
		assertEquals("https://image.tmdb.org/t/p/w342/poster.jpg", decoded.posterUrl)
		assertNull(decoded.backdropUrl)
		assertEquals("8.7", decoded.rating)
	}

	@Test
	fun toleratesMissingOptionalFields() {
		// TMDB omits fields freely; every default here is load-bearing.
		val decoded = json.decodeFromString<Movie>("""{"id":1,"title":"X"}""")

		assertEquals("", decoded.releaseDate)
		assertEquals("0.0", decoded.rating)
	}
}
