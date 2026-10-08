/*
 * Cinelex
 * SafeApiCallTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SafeApiCallTest {
	private suspend fun call(mock: MockClient) = safeApiCall { mock.client.get("anything").bodyAsText() }

	private suspend fun error(mock: MockClient): CinelexException = assertFailsWith<CinelexException> { call(mock) }

	@Test
	fun returnsSuccessfulBody() =
		runTest {
			val body = """{"status_code":1,"status_message":"ok"}"""

			assertEquals(body, call(MockClient(body = body)))
		}

	@Test
	fun mapsUnauthorizedToFrozenCode() =
		runTest {
			val error =
				error(
					MockClient(
						status = HttpStatusCode.Unauthorized,
						body = """{"status_code":7,"status_message":"Invalid API key"}""",
					),
				)

			assertEquals(ErrorConstants.HTTP_UNAUTHORIZED, error.code)
			assertEquals("Invalid API key", error.message)
		}

	@Test
	fun mapsForbiddenWithOurOwnCopy() =
		runTest {
			val error = error(MockClient(status = HttpStatusCode.Forbidden))

			assertEquals(ErrorConstants.HTTP_FORBIDDEN, error.code)
		}

	@Test
	fun mapsServerErrorRange() =
		runTest {
			val error = error(MockClient(status = HttpStatusCode.BadGateway))

			assertEquals(ErrorConstants.UNKNOWN_ERROR, error.code)
			assertEquals("Server error. Please try again later.", error.message)
		}

	@Test
	fun survivesUnparseableErrorBody() =
		runTest {
			val error = error(MockClient(status = HttpStatusCode.BadRequest, body = "<html>nope</html>"))

			assertEquals("An unexpected error occurred.", error.message)
		}
}
