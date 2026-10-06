/*
 * Cinelex
 * WatchlistDao
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.esekiels.cinelex.database.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
	@Query("SELECT * FROM WatchlistEntity ORDER BY added_at DESC")
	fun observeAll(): Flow<List<WatchlistEntity>>

	@Query("SELECT EXISTS(SELECT 1 FROM WatchlistEntity WHERE id = :id)")
	fun observeIsSaved(id: Int): Flow<Boolean>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun save(entity: WatchlistEntity)

	@Query("DELETE FROM WatchlistEntity WHERE id = :id")
	suspend fun delete(id: Int)
}
