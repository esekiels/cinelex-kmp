/*
 * Cinelex
 * DatabaseFactory
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun buildDatabase(builder: RoomDatabase.Builder<CinelexDatabase>): CinelexDatabase =
	builder
		.setDriver(BundledSQLiteDriver())
		.fallbackToDestructiveMigrationFrom(dropAllTables = true, 1, 2, 3)
		.build()
