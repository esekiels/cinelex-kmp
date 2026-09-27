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
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val datastorePlatformModule: Module =
	module {
		single<DataStore<Preferences>> {
			createDataStore { androidContext().filesDir.resolve(DATASTORE_FILE).absolutePath }
		}
	}
