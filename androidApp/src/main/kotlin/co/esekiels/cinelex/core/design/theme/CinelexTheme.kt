/*
 * Cinelex
 * CinelexTheme
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CinelexTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	MaterialTheme(
		colorScheme = if (darkTheme) DarkColors else LightColors,
		typography = CinelexTypography,
		content = content,
	)
}
