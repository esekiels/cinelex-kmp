/*
 * Cinelex
 * MovieService
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.service

import co.esekiels.cinelex.network.model.MovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MovieService(private val client: HttpClient) {

    suspend fun fetchMovies(category: String, language: String, page: Int = 1): MovieResponse =
        client.get(category) {
            parameter("language", language)
            parameter("page", page)
        }.body()
}
