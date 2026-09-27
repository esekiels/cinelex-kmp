/*
 * Cinelex
 * DatastoreModule
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.esekiels.cinelex.datastore.DATASTORE_FILE
import co.esekiels.cinelex.datastore.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val datastorePlatformModule: Module =
	module {
		single<DataStore<Preferences>> {
			createDataStore { documentDirectory() + "/" + DATASTORE_FILE }
		}
	}

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
