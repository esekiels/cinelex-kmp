/*
 * Cinelex
 * MockClient
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal class MockClient(
	status: HttpStatusCode = HttpStatusCode.OK,
	body: String = "",
) {
	val engine =
		MockEngine { request ->
			requests += request.url.toString()
			respond(
				content = body,
				status = status,
				headers = headersOf(HttpHeaders.ContentType, "application/json"),
			)
		}

	val requests = mutableListOf<String>()

	val client: HttpClient = createHttpClient(engine = engine)
}
