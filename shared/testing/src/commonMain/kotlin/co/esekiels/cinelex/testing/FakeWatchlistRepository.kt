/*
 * Cinelex
 * FakeWatchlistRepository
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.data.WatchlistRepository
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeWatchlistRepository(
	initial: List<Movie> = emptyList(),
) : WatchlistRepository {
	/** Swift-only: Kotlin default arguments are not exported across the ObjC bridge. */
	constructor() : this(emptyList())

	val movies = MutableStateFlow(initial)

	override fun observeWatchlist(): Flow<List<Movie>> = movies

	override fun observeIsInWatchlist(id: Int) = movies.map { it.any { movie -> movie.id == id } }.distinctUntilChanged()

	override suspend fun add(movie: Movie) {
		movies.update { list -> if (list.any { it.id == movie.id }) list else listOf(movie) + list }
	}

	override suspend fun remove(id: Int) {
		movies.update { list -> list.filterNot { it.id == id } }
	}
}
