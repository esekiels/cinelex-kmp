package co.esekiels.cinelex.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

internal fun testDatabase(): CinelexDatabase =
	Room
		.inMemoryDatabaseBuilder<CinelexDatabase>()
		.setDriver(BundledSQLiteDriver())
		.build()
