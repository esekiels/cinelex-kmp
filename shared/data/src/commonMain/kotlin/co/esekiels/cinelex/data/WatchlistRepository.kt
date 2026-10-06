/*
 * Cinelex
 * WatchlistRepository
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.model.Movie
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
	/** Newest first, as saved: titles stay in the language they were saved in. */
	@NativeCoroutines
	fun observeWatchlist(): Flow<List<Movie>>

	@NativeCoroutines
	fun observeIsInWatchlist(id: Int): Flow<Boolean>

	@NativeCoroutines
	suspend fun add(movie: Movie)

	@NativeCoroutines
	suspend fun remove(id: Int)
}
