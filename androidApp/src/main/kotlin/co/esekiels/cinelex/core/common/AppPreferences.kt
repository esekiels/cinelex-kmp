/*
 * Cinelex
 * AppPreferences
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import java.util.Locale

fun applyUiTheme(uiTheme: UiTheme) {
	AppCompatDelegate.setDefaultNightMode(
		when (uiTheme) {
			UiTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
			UiTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
			UiTheme.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		},
	)
}

fun applyLanguage(language: Language?) {
	AppCompatDelegate.setApplicationLocales(
		language?.let { LocaleListCompat.forLanguageTags(it.code) } ?: LocaleListCompat.getEmptyLocaleList(),
	)
}

fun systemAppLanguage(): Language? = AppCompatDelegate.getApplicationLocales()[0]?.toLanguage()

fun Locale.toLanguage(): Language? = Language.fromCode(toLanguageTag().substringBefore('-'))
