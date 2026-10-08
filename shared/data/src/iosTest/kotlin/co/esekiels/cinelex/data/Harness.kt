/*
 * Cinelex
 * Harness
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import co.esekiels.cinelex.database.CinelexDatabase
import co.esekiels.cinelex.network.createHttpClient
import co.esekiels.cinelex.network.service.MovieClient
import co.esekiels.cinelex.network.service.MovieService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.Dispatchers
import kotlinx.io.IOException

/**
 * Real Room, fake network. The repository's job is the cache policy, so the
 * database has to be the real one — a fake DAO would only prove the fake works.
 *
 * Lives in iosTest because Room's no-arg in-memory builder is native-only;
 * the Android overload needs a Context, which commonTest cannot supply.
 */
internal class Harness {
	private class Reply(
		val status: HttpStatusCode,
		val body: String,
		val offline: Boolean = false,
	)

	private var reply = Reply(HttpStatusCode.OK, EMPTY_JSON)

	val requests = mutableListOf<String>()

	val database: CinelexDatabase =
		Room
			.inMemoryDatabaseBuilder<CinelexDatabase>()
			.setDriver(BundledSQLiteDriver())
			.build()

	val dao = database.movieDao()

	private val engine =
		MockEngine(
			MockEngineConfig().apply {
				dispatcher = Dispatchers.Unconfined
				addHandler { request ->
					requests += request.url.toString()
					if (reply.offline) throw IOException("simulated offline")
					respond(reply.body, reply.status, headersOf(HttpHeaders.ContentType, "application/json"))
				}
			},
		)

	val movieClient = MovieClient(MovieService(createHttpClient(engine = engine)))

	fun alwaysRespond(
		body: String,
		status: HttpStatusCode = HttpStatusCode.OK,
	) {
		reply = Reply(status, body)
	}

	fun alwaysOffline() {
		reply = Reply(HttpStatusCode.OK, EMPTY_JSON, offline = true)
	}

	fun close() = database.close()

	private companion object {
		const val EMPTY_JSON = "{}"
	}
}

internal const val MOVIE_PAGE = """
{
  "page": 1,
  "total_pages": 1,
  "results": [
    { "id": 278, "title": "The Shawshank Redemption", "poster_path": "/p.jpg", "backdrop_path": "/b.jpg" }
  ]
}
"""

internal const val MOVIE_PAGE_UPDATED = """
{
  "page": 1,
  "total_pages": 1,
  "results": [
    { "id": 999, "title": "A Newer Film", "poster_path": "/n.jpg", "backdrop_path": "/nb.jpg" }
  ]
}
"""
