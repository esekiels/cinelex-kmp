/*
 * Cinelex
 * HttpClientFactory
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIMEOUT_MILLIS = 30_000L

val cinelexJson: Json =
	Json {
		ignoreUnknownKeys = true
		coerceInputValues = true
	}

fun createHttpClient(
	json: Json = cinelexJson,
	enableLogging: Boolean = false,
	engine: HttpClientEngine? = null,
): HttpClient {
	val config: HttpClientConfig<*>.() -> Unit = {
		expectSuccess = true

		install(ContentNegotiation) { json(json) }

		install(HttpTimeout) {
			requestTimeoutMillis = TIMEOUT_MILLIS
			connectTimeoutMillis = TIMEOUT_MILLIS
			socketTimeoutMillis = TIMEOUT_MILLIS
		}

		defaultRequest {
			url(BuildKonfig.BASE_URL)
			header(HttpHeaders.Authorization, "Bearer ${BuildKonfig.TMDB_TOKEN}")
			header(HttpHeaders.Accept, ContentType.Application.Json)
		}

		if (enableLogging) {
			install(Logging) { level = LogLevel.BODY }
		}
	}

	// No engine → Ktor picks the one on the classpath (OkHttp on Android, Darwin on iOS).
	return engine?.let { HttpClient(it, config) } ?: HttpClient(config)
}
