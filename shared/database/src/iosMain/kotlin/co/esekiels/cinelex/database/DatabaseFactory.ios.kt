/*
 * Cinelex
 * DatabaseFactory
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun databaseBuilder(): RoomDatabase.Builder<CinelexDatabase> =
	Room.databaseBuilder<CinelexDatabase>(name = documentDirectory() + "/" + DATABASE_NAME)

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
	val url =
		NSFileManager.defaultManager.URLForDirectory(
			directory = NSDocumentDirectory,
			inDomain = NSUserDomainMask,
			appropriateForURL = null,
			create = false,
			error = null,
		)
	return requireNotNull(url?.path) { "Could not resolve iOS document directory" }
}
