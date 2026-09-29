/*
 * Cinelex
 * MovieRepositoryImpl
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toEntities
import co.esekiels.cinelex.database.entity.mapper.toEntity
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.network.ApiConstants
import co.esekiels.cinelex.network.ApiResponse
import co.esekiels.cinelex.network.service.MovieClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal val MOVIE_CATEGORIES =
	listOf(
		ApiConstants.NOW_PLAYING,
		ApiConstants.UPCOMING,
		ApiConstants.TOP_RATED,
		ApiConstants.POPULAR,
	)

internal class MovieRepositoryImpl(
	private val client: MovieClient,
	private val dao: MovieDao,
	private val ioDispatcher: CoroutineDispatcher,
) : MovieRepository {
	override fun observeNowPlaying(): Flow<List<Movie>> = observe(ApiConstants.NOW_PLAYING)

	override fun observePopular(): Flow<List<Movie>> = observe(ApiConstants.POPULAR)

	override fun observeUpcoming(): Flow<List<Movie>> = observe(ApiConstants.UPCOMING)

	override fun observeTopRated(): Flow<List<Movie>> = observe(ApiConstants.TOP_RATED)

	override suspend fun refreshMovies() =
		withContext(ioDispatcher) {
			for (category in MOVIE_CATEGORIES) {
				when (val response = client.fetchMovies(category, "en-us")) {
					is ApiResponse.Success ->
						dao.replaceCategory(category, response.body.results.toEntities(category))

					is ApiResponse.Failure -> throw response.error
				}
			}
		}

	override suspend fun fetchMovieDetails(id: Int): MovieDetails =
		withContext(ioDispatcher) {
			when (val response = client.fetchDetails(id, "en-us")) {
				is ApiResponse.Success -> response.body.also { dao.saveDetails(it.toEntity()) }
				is ApiResponse.Failure -> dao.fetchDetails(id)?.toDomain() ?: throw response.error
			}
		}

	private fun observe(category: String): Flow<List<Movie>> = dao.observeMovieByCategory(category).map { it.toDomain() }
}
