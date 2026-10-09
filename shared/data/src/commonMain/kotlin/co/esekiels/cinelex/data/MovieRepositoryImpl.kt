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
import co.esekiels.cinelex.network.service.MovieService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

internal class MovieRepositoryImpl(
	private val service: MovieService,
	private val dao: MovieDao,
	language: Flow<Language>,
	private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
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
						val movies = service.fetchMovies(category, code, page = 1).results
						dao.replaceCategory(category, code, movies.toEntities(category, code))
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
			dao.saveDetails(service.fetchDetails(id, code).toEntity(code))
		}

	override suspend fun searchMovies(
		query: String,
		page: Int,
	): SearchResult =
		ioDispatcher.guarded {
			val response = service.searchMovies(query, language.first().code, page)
			SearchResult(response.results.distinctBy { it.id }, response.totalPages)
		}
}
