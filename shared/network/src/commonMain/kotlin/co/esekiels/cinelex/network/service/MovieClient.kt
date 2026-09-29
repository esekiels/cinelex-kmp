/*
 * Cinelex
 * MovieClient
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.service

import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.network.ApiResponse
import co.esekiels.cinelex.network.model.MovieResponse
import co.esekiels.cinelex.network.safeApiCall

class MovieClient(
	private val service: MovieService,
) {
	suspend fun fetchMovies(
		category: String,
		language: String,
		page: Int = 1,
	): ApiResponse<MovieResponse> = safeApiCall { service.fetchMovies(category, language, page) }

	suspend fun fetchDetails(
		id: Int,
		language: String,
	): ApiResponse<MovieDetails> = safeApiCall { service.fetchDetails(id, language) }
}
