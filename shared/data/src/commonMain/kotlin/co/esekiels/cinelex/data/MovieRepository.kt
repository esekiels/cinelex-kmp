/*
 * Cinelex
 * MovieRepository
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
	@NativeCoroutines
	fun observeNowPlaying(): Flow<List<Movie>>

	@NativeCoroutines
	fun observePopular(): Flow<List<Movie>>

	@NativeCoroutines
	fun observeUpcoming(): Flow<List<Movie>>

	@NativeCoroutines
	fun observeTopRated(): Flow<List<Movie>>

	@NativeCoroutines
	suspend fun refreshMovies()

	@NativeCoroutines
	suspend fun fetchMovieDetails(id: Int): MovieDetails
}
