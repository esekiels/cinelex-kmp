/*
 * Cinelex
 * FakeMovieRepository
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow

class FakeMovieRepository(
	nowPlaying: List<Movie> = MovieStubs.all,
	private val isLoading: Boolean = false,
) : MovieRepository {
	/** Swift-only: Kotlin default arguments are not exported across the ObjC bridge. */
	constructor(nowPlaying: List<Movie>) : this(nowPlaying, isLoading = false)

	private val nowPlayingFlow = MutableStateFlow(nowPlaying)
	private val upcomingFlow = MutableStateFlow(nowPlaying)
	private val topRatedFlow = MutableStateFlow(nowPlaying)
	private val popularFlow = MutableStateFlow(nowPlaying)

	var failure: CinelexException? = null

	var refreshCount: Int = 0
		private set

	override fun observeNowPlaying(): Flow<List<Movie>> = nowPlayingFlow.orNever()

	override fun observeUpcoming(): Flow<List<Movie>> = upcomingFlow.orNever()

	override fun observeTopRated(): Flow<List<Movie>> = topRatedFlow.orNever()

	override fun observePopular(): Flow<List<Movie>> = popularFlow.orNever()

	override suspend fun refreshMovies() {
		refreshCount++
		if (isLoading) awaitCancellation()
		failure?.let { throw it }
	}

	private fun MutableStateFlow<List<Movie>>.orNever(): Flow<List<Movie>> = if (isLoading) emptyFlow() else asStateFlow()
}
