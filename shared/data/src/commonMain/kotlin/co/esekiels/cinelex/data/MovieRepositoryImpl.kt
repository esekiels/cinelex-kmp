/*
 * Cinelex
 * MovieRepositoryImpl
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toDomainOrNull
import co.esekiels.cinelex.database.entity.mapper.toEntities
import co.esekiels.cinelex.database.entity.mapper.toEntity
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.network.ApiConstants
import co.esekiels.cinelex.network.ApiResponse
import co.esekiels.cinelex.network.service.MovieClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal val MOVIE_CATEGORIES =
	listOf(
		ApiConstants.NOW_PLAYING,
		ApiConstants.UPCOMING,
		ApiConstants.TOP_RATED,
		ApiConstants.POPULAR,
	)

@OptIn(ExperimentalCoroutinesApi::class)
internal class MovieRepositoryImpl(
	private val client: MovieClient,
	private val dao: MovieDao,
	language: Flow<Language>,
	private val ioDispatcher: CoroutineDispatcher,
) : MovieRepository {
	private val language = language.distinctUntilChanged()

	override fun observeNowPlaying(): Flow<List<Movie>> = observe(ApiConstants.NOW_PLAYING)

	override fun observePopular(): Flow<List<Movie>> = observe(ApiConstants.POPULAR)

	override fun observeUpcoming(): Flow<List<Movie>> = observe(ApiConstants.UPCOMING)

	override fun observeTopRated(): Flow<List<Movie>> = observe(ApiConstants.TOP_RATED)

	override suspend fun refreshMovies() =
		ioDispatcher.guarded {
			val code = language.first().code
			for (category in MOVIE_CATEGORIES) {
				when (val response = client.fetchMovies(category, code)) {
					is ApiResponse.Success ->
						dao.replaceCategory(category, code, response.body.results.toEntities(category, code))

					is ApiResponse.Failure -> throw response.error
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
				is ApiResponse.Success -> SearchResult(response.body.results, response.body.totalPages)
				is ApiResponse.Failure -> throw response.error
			}
		}

	private fun observe(category: String): Flow<List<Movie>> =
		language
			.flatMapLatest { dao.observeMovieByCategory(category, it.code) }
			.map { it.toDomain() }
			.distinctUntilChanged()
			.catch { throw it.toCinelexException() }
}

@Suppress("TooGenericExceptionCaught")
private suspend fun <T> CoroutineDispatcher.guarded(block: suspend () -> T): T =
	try {
		withContext(this) { block() }
	} catch (e: CancellationException) {
		throw e
	} catch (e: Exception) {
		throw e.toCinelexException()
	}

private fun Throwable.toCinelexException(): CinelexException =
	this as? CinelexException
		?: CinelexException(ErrorConstants.UNKNOWN_ERROR, message ?: "An unexpected error occurred.", this)
