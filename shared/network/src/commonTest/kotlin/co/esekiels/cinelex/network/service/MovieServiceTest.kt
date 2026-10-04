/*
 * Cinelex
 * MovieServiceTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.service

import co.esekiels.cinelex.network.ApiConstants
import co.esekiels.cinelex.network.MOVIE_DETAILS_RESPONSE
import co.esekiels.cinelex.network.MOVIE_RESPONSE
import co.esekiels.cinelex.network.MockClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovieServiceTest {
	@Test
	fun decodesEveryCarouselEndpoint() =
		runTest {
			val categories =
				listOf(
					ApiConstants.NOW_PLAYING,
					ApiConstants.UPCOMING,
					ApiConstants.TOP_RATED,
					ApiConstants.POPULAR,
				)

			for (category in categories) {
				val mock = MockClient(body = MOVIE_RESPONSE)
				val response = MovieService(mock.client).fetchMovies(category, "en")

				assertEquals(2, response.results.size, "failed for $category")
				assertEquals("The Shawshank Redemption", response.results.first().title)
				assertTrue(mock.requests.single().contains(category))
			}
		}

	@Test
	fun decodesDetailsWithCreditsAndVideos() =
		runTest {
			val mock = MockClient(body = MOVIE_DETAILS_RESPONSE)
			val details = MovieService(mock.client).fetchDetails(278, "en")

			assertEquals("The Shawshank Redemption", details.title)
			assertEquals(listOf("Frank Darabont"), details.directors.map { it.name })
			assertEquals(1, details.youtubeTrailers.size)
			assertTrue(mock.requests.single().contains("movie/278"))
			assertTrue(mock.requests.single().contains("append_to_response=credits%2Cvideos"))
			assertTrue(mock.requests.single().contains("include_video_language=en%2Cen%2Cnull"))
		}
}
