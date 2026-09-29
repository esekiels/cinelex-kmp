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
	fun formatsRuntimeGenresAndYear() {
		assertEquals("2h 22m", details.durationFormatted)
		assertEquals("Drama, Crime", details.genreFormatted)
		assertEquals("1994", details.releaseYearFormatted)
		assertEquals("N/A", details.copy(runtime = null).durationFormatted)
		assertEquals("N/A", details.copy(genres = emptyList()).genreFormatted)
		assertEquals("N/A", details.copy(releaseDate = "").releaseYearFormatted)
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
