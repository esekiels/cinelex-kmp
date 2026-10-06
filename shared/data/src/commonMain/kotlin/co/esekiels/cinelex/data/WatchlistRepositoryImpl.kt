/*
 * Cinelex
 * WatchlistRepositoryImpl
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.database.dao.WatchlistDao
import co.esekiels.cinelex.database.entity.mapper.toDomain
import co.esekiels.cinelex.database.entity.mapper.toWatchlistEntity
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

internal class WatchlistRepositoryImpl(
	private val dao: WatchlistDao,
	private val ioDispatcher: CoroutineDispatcher,
) : WatchlistRepository {
	override fun observeWatchlist(): Flow<List<Movie>> =
		dao
			.observeAll()
			.map { entities -> entities.map { it.toDomain() } }
			.distinctUntilChanged()
			.catch { throw it.toCinelexException() }

	override fun observeIsInWatchlist(id: Int): Flow<Boolean> =
		dao
			.observeIsSaved(id)
			.distinctUntilChanged()
			.catch { throw it.toCinelexException() }

	override suspend fun add(movie: Movie) =
		ioDispatcher.guarded { dao.save(movie.toWatchlistEntity(Clock.System.now().toEpochMilliseconds())) }

	override suspend fun remove(id: Int) = ioDispatcher.guarded { dao.delete(id) }
}
