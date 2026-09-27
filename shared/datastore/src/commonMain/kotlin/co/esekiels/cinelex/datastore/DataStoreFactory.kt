/*
 * Cinelex
 * DataStoreFactory
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import okio.Path.Companion.toPath

internal const val DATASTORE_FILE = "cinelex.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
	PreferenceDataStoreFactory.createWithPath(
		corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
		produceFile = { producePath().toPath() },
	)
