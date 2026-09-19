/*
 * Cinelex
 * MovieServiceTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.service

import co.esekiels.cinelex.network.ApiConstants
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
}
