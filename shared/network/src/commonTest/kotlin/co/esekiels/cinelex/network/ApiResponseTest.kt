/*
 * Cinelex
 * ApiResponseTest
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
import kotlin.test.assertIs

class ApiResponseTest {

    private suspend fun call(mock: MockClient) = safeApiCall { mock.client.get("anything").bodyAsText() }

    private fun ApiResponse<*>.error(): CinelexException = assertIs<ApiResponse.Failure>(this).error

    @Test
    fun wrapsSuccessfulBody() = runTest {
        val result = call(MockClient(body = """{"status_code":1,"status_message":"ok"}"""))

        assertIs<ApiResponse.Success<String>>(result)
    }

    @Test
    fun mapTransformsSuccessAndKeepsFailure() = runTest {
        assertEquals(ApiResponse.Success(2), ApiResponse.Success("ok").map { it.length })

        val failure = call(MockClient(status = HttpStatusCode.BadGateway))
        assertEquals(failure.error(), failure.map { it.length }.error())
    }

    @Test
    fun mapsUnauthorizedToFrozenCode() = runTest {
        val result = call(
            MockClient(
                status = HttpStatusCode.Unauthorized,
                body = """{"status_code":7,"status_message":"Invalid API key"}""",
            ),
        )

        val error = result.error()
        assertEquals(ErrorConstants.HTTP_UNAUTHORIZED, error.code)
        assertEquals("Invalid API key", error.message)
    }

    @Test
    fun mapsForbiddenWithOurOwnCopy() = runTest {
        val result = call(MockClient(status = HttpStatusCode.Forbidden))

        assertEquals(ErrorConstants.HTTP_FORBIDDEN, result.error().code)
    }

    @Test
    fun mapsServerErrorRange() = runTest {
        val error = call(MockClient(status = HttpStatusCode.BadGateway)).error()

        assertEquals(ErrorConstants.UNKNOWN_ERROR, error.code)
        assertEquals("Server error. Please try again later.", error.message)
    }

    @Test
    fun survivesUnparseableErrorBody() = runTest {
        val result = call(MockClient(status = HttpStatusCode.BadRequest, body = "<html>nope</html>"))

        assertEquals("An unexpected error occurred.", result.error().message)
    }
}
