/*
 * Cinelex
 * MovieDetailsTest
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MovieDetailsTest {
	private val details =
		MovieDetails(
			id = 278,
			title = "The Shawshank Redemption",
			overview = "Two imprisoned men bond over a number of years.",
			voteAverage = 8.7,
			releaseDate = "1994-09-23",
			runtime = 142,
			genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
			credits =
				Credits(
					cast = listOf(Cast(1, "Tim Robbins", "Andy Dufresne", "/tim.jpg")),
					crew =
						listOf(
							Crew(2, "Frank Darabont", "Director"),
							Crew(3, "Niki Marvin", "Producer"),
							Crew(4, "Frank Darabont", "Screenplay"),
						),
				),
			videos =
				VideoResponse(
					listOf(
						Video("v1", "abc", "Trailer", "YouTube", "Trailer"),
						Video("v2", "def", "Clip", "Vimeo", "Trailer"),
						Video("v3", "ghi", "BTS", "YouTube", "Featurette"),
					),
				),
		)

	@Test
	fun exposesRuntimeGenresAndYearForFormatting() {
		assertEquals(Length(hours = 2, minutes = 22), details.length)
		assertEquals("Drama, Crime", details.genreNames)
		assertEquals("1994", details.releaseYear)
		assertNull(details.copy(runtime = null).length)
		assertNull(details.copy(genres = emptyList()).genreNames)
		assertNull(details.copy(releaseDate = "").releaseYear)
	}

	@Test
	fun splitsCrewByJob() {
		assertEquals(listOf("Frank Darabont"), details.directors.map { it.name })
		assertEquals(listOf("Niki Marvin"), details.producers.map { it.name })
		assertEquals(listOf("Frank Darabont"), details.screenwriters.map { it.name })
	}

	@Test
	fun keepsOnlyYoutubeTrailers() {
		assertEquals(listOf("abc"), details.youtubeTrailers.map { it.key })
	}

	@Test
	fun fillsStarsUpToRoundedScore() {
		assertTrue(details.isStarFilled(8))
		assertFalse(details.isStarFilled(9))
	}
}
