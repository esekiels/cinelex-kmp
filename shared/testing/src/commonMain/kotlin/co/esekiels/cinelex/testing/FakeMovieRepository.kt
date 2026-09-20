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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMovieRepository(
	nowPlaying: List<Movie> = MovieStubs.all,
) : MovieRepository {
	private val nowPlayingFlow = MutableStateFlow(nowPlaying)
	private val upcomingFlow = MutableStateFlow(nowPlaying)
	private val topRatedFlow = MutableStateFlow(nowPlaying)
	private val popularFlow = MutableStateFlow(nowPlaying)

	var failure: CinelexException? = null

	var refreshCount: Int = 0
		private set

	override fun observeNowPlaying(): Flow<List<Movie>> = nowPlayingFlow.asStateFlow()

	override fun observeUpcoming(): Flow<List<Movie>> = upcomingFlow.asStateFlow()

	override fun observeTopRated(): Flow<List<Movie>> = topRatedFlow.asStateFlow()

	override fun observePopular(): Flow<List<Movie>> = popularFlow.asStateFlow()

	override suspend fun refreshMovies() {
		refreshCount++
		failure?.let { throw it }
	}
}
