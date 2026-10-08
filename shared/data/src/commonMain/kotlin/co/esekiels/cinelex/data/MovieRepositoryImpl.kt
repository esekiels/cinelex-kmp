/*
 * Cinelex
 * MovieRepositoryImpl
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toDomainOrNull
import co.esekiels.cinelex.database.entity.mapper.toEntities
import co.esekiels.cinelex.database.entity.mapper.toEntity
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieCategory
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.network.ApiConstants
import co.esekiels.cinelex.network.ApiResponse
import co.esekiels.cinelex.network.service.MovieClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val MovieCategory.path: String
	get() =
		when (this) {
			MovieCategory.NOW_PLAYING -> ApiConstants.NOW_PLAYING
			MovieCategory.UPCOMING -> ApiConstants.UPCOMING
			MovieCategory.TOP_RATED -> ApiConstants.TOP_RATED
			MovieCategory.POPULAR -> ApiConstants.POPULAR
		}

internal val MOVIE_CATEGORIES = MovieCategory.entries.map { it.path }

@OptIn(ExperimentalCoroutinesApi::class)
internal class MovieRepositoryImpl(
	private val client: MovieClient,
	private val dao: MovieDao,
	language: Flow<Language>,
	private val ioDispatcher: CoroutineDispatcher,
) : MovieRepository {
	private val language = language.distinctUntilChanged()

	override fun observeMovies(category: MovieCategory): Flow<List<Movie>> =
		language
			.flatMapLatest { dao.observeMovieByCategory(category.path, it.code) }
			.map { it.toDomain() }
			.distinctUntilChanged()
			.catch { throw it.toCinelexException() }

	override suspend fun refreshMovies() =
		ioDispatcher.guarded {
			val code = language.first().code
			coroutineScope {
				for (category in MOVIE_CATEGORIES) {
					launch {
						when (val response = client.fetchMovies(category, code)) {
							is ApiResponse.Success ->
								dao.replaceCategory(category, code, response.body.results.toEntities(category, code))

							is ApiResponse.Failure -> throw response.error
						}
					}
				}
			}
		}

	override suspend fun hasCachedMovies(): Boolean = ioDispatcher.guarded { dao.countMovies(language.first().code) > 0 }

	override fun observeContentLanguage(): Flow<Language> = language

	override fun observeMovieDetails(id: Int): Flow<MovieDetails?> =
		language
			.flatMapLatest { dao.observeDetails(id, it.code) }
			.map { it?.toDomainOrNull() }
			.distinctUntilChanged()
			.catch { throw it.toCinelexException() }

	override suspend fun refreshMovieDetails(id: Int) =
		ioDispatcher.guarded {
			val code = language.first().code
			when (val response = client.fetchDetails(id, code)) {
				is ApiResponse.Success -> dao.saveDetails(response.body.toEntity(code))
				is ApiResponse.Failure -> throw response.error
			}
		}

	override suspend fun searchMovies(
		query: String,
		page: Int,
	): SearchResult =
		ioDispatcher.guarded {
			when (val response = client.searchMovies(query, language.first().code, page)) {
				is ApiResponse.Success -> SearchResult(response.body.results.distinctBy { it.id }, response.body.totalPages)
				is ApiResponse.Failure -> throw response.error
			}
		}
}
