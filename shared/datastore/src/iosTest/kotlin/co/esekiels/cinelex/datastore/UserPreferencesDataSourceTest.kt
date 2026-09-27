/*
 * Cinelex
 * UserPreferencesDataSourceTest
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.datastore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserPreferencesDataSourceTest {
	private val path = NSTemporaryDirectory() + NSUUID().UUIDString() + ".preferences_pb"
	private val dataStore = createDataStore { path }
	private val sut = UserPreferencesDataSource(dataStore)

	@Test
	fun defaultsToDeviceLanguageAndFollowSystem() =
		runTest {
			assertEquals(UserPreferences(), sut.data.first())
		}

	@Test
	fun persistsLanguageAndTheme() =
		runTest {
			sut.setLanguage(Language.INDONESIAN)
			sut.setUiTheme(UiTheme.DARK)

			assertEquals(UserPreferences(Language.INDONESIAN, UiTheme.DARK), sut.data.first())
		}

	@Test
	fun nullLanguageClearsIt() =
		runTest {
			sut.setLanguage(Language.INDONESIAN)
			sut.setLanguage(null)

			assertNull(sut.data.first().language)
		}

	@Test
	fun unknownStoredValuesFallBack() =
		runTest {
			dataStore.edit {
				it[stringPreferencesKey("ui_theme")] = "SEPIA"
				it[stringPreferencesKey("language")] = "xx"
			}

			assertEquals(UserPreferences(), sut.data.first())
		}

	@Test
	fun corruptFileFallsBackToDefaults() =
		runTest {
			FileSystem.SYSTEM.write(path.toPath()) { writeUtf8("not a protobuf") }

			assertEquals(UserPreferences(), sut.data.first())
		}
}
