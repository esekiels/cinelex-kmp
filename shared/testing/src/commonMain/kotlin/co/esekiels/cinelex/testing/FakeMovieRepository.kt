/*
 * Cinelex
 * FakeMovieRepository
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.SearchResult
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieCategory
import co.esekiels.cinelex.model.MovieDetails
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeMovieRepository(
	nowPlaying: List<Movie> = MovieStubs.all,
	private val isLoading: Boolean = false,
) : MovieRepository {
	/** Swift-only: Kotlin default arguments are not exported across the ObjC bridge. */
	constructor(nowPlaying: List<Movie>) : this(nowPlaying, isLoading = false)

	private val moviesFlow = MutableStateFlow(nowPlaying)
	private val detailsFlow = MutableStateFlow<Map<Int, MovieDetails>>(emptyMap())

	val contentLanguage = MutableStateFlow(Language.ENGLISH)

	var failure: CinelexException? = null

	var detailsFailure: CinelexException? = null

	var refreshCount: Int = 0
		private set

	override fun observeMovies(category: MovieCategory): Flow<List<Movie>> = if (isLoading) emptyFlow() else moviesFlow

	override suspend fun refreshMovies() {
		refreshCount++
		if (isLoading) awaitCancellation()
		failure?.let { throw it }
	}

	override suspend fun hasCachedMovies(): Boolean = moviesFlow.value.isNotEmpty()

	override fun observeContentLanguage(): Flow<Language> = contentLanguage

	override fun observeMovieDetails(id: Int): Flow<MovieDetails?> = detailsFlow.map { it[id] }.distinctUntilChanged()

	override suspend fun refreshMovieDetails(id: Int) {
		detailsFailure?.let { throw it }
		detailsFlow.update { it + (id to MovieStubs.details(id)) }
	}

	override suspend fun searchMovies(
		query: String,
		page: Int,
	): SearchResult {
		failure?.let { throw it }
		val matches = moviesFlow.value.filter { it.title.contains(query, ignoreCase = true) }
		return SearchResult(listOfNotNull(matches.getOrNull(page - 1)), totalPages = matches.size)
	}
}
