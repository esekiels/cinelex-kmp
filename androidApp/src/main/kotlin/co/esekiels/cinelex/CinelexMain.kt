/*
 * Cinelex
 * CinelexMain
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex

import androidx.compose.runtime.Composable
import co.esekiels.cinelex.core.design.components.LanguageMenu
import co.esekiels.cinelex.core.design.components.ThemeMenu
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.feature.home.HomeScreen
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences

@Composable
fun CinelexMain(
	preferences: UserPreferences,
	onLanguageSelected: (Language) -> Unit,
	onThemeSelected: (UiTheme) -> Unit,
) {
	CinelexTheme {
		HomeScreen {
			LanguageMenu(preferences.language, onLanguageSelected)
			ThemeMenu(preferences.uiTheme, onThemeSelected)
		}
	}
}
