/*
 * Cinelex
 * UserPreferencesDataSource
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val KEY_LANGUAGE = stringPreferencesKey("language")
private val KEY_UI_THEME = stringPreferencesKey("ui_theme")

class UserPreferencesDataSource(
	private val dataStore: DataStore<Preferences>,
) {
	val data: Flow<UserPreferences> = dataStore.data.map { it.toUserPreferences() }

	suspend fun setLanguage(language: Language?) {
		dataStore.edit {
			if (language == null) it.remove(KEY_LANGUAGE) else it[KEY_LANGUAGE] = language.code
		}
	}

	suspend fun setUiTheme(uiTheme: UiTheme) {
		dataStore.edit { it[KEY_UI_THEME] = uiTheme.name }
	}
}

private fun Preferences.toUserPreferences() =
	UserPreferences(
		language = Language.fromCode(this[KEY_LANGUAGE]),
		uiTheme = UiTheme.entries.firstOrNull { it.name == this[KEY_UI_THEME] } ?: UiTheme.FOLLOW_SYSTEM,
	)
