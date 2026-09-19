/*
 * Cinelex
 * CinelexDatabase
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import co.esekiels.cinelex.database.dao.MovieDao
import co.esekiels.cinelex.database.entity.MovieEntity

internal const val DATABASE_NAME = "cinelex.db"

@Database(entities = [MovieEntity::class], version = 1)
@ConstructedBy(CinelexDatabaseConstructor::class)
abstract class CinelexDatabase : RoomDatabase() {
	abstract fun movieDao(): MovieDao
}

@Suppress("KotlinNoActualForExpect")
expect object CinelexDatabaseConstructor : RoomDatabaseConstructor<CinelexDatabase> {
	override fun initialize(): CinelexDatabase
}
