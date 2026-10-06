/*
 * Cinelex
 * CinelexDatabase
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.dao.WatchlistDao
import co.esekiels.cinelex.database.entity.MovieDetailsEntity
import co.esekiels.cinelex.database.entity.MovieEntity
import co.esekiels.cinelex.database.entity.WatchlistEntity

internal const val DATABASE_NAME = "cinelex.db"

// The watchlist is user data: every version bump from 5 on needs a migration, or the destructive fallback wipes it.
@Database(
	entities = [MovieEntity::class, MovieDetailsEntity::class, WatchlistEntity::class],
	version = 5,
	autoMigrations = [AutoMigration(from = 4, to = 5)],
)
@ConstructedBy(CinelexDatabaseConstructor::class)
abstract class CinelexDatabase : RoomDatabase() {
	abstract fun movieDao(): MovieDao

	abstract fun watchlistDao(): WatchlistDao
}

@Suppress("KotlinNoActualForExpect")
expect object CinelexDatabaseConstructor : RoomDatabaseConstructor<CinelexDatabase> {
	override fun initialize(): CinelexDatabase
}
