/*
 * Cinelex
 * CinelexTheme
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import co.esekiels.cinelex.R

private val CinelexPrimary = Color(0xFF075E54)
private val CinelexPrimaryDark = Color(0xFF00A884)

private val DarkBackground = Color(0xFF111B21)
private val DarkTextPrimary = Color(0xFFE9EDEF)
private val DarkTextSecondary = Color(0xFF8696A0)

private val LightBackground = Color(0xFFFFFFFF)
private val LightTextPrimary = Color(0xFF111B21)
private val LightTextSecondary = Color(0xFF667781)

private val LightColors =
	lightColorScheme(
		primary = CinelexPrimary,
		onPrimary = Color.White,
		secondary = CinelexPrimary,
		onSecondary = Color.White,
		background = LightBackground,
		onBackground = LightTextPrimary,
		surface = LightBackground,
		onSurface = LightTextPrimary,
		onSurfaceVariant = LightTextSecondary,
		surfaceContainerLowest = Color(0xFFFFFFFF),
		surfaceContainerLow = Color(0xFFF7F8FA),
		surfaceContainer = Color(0xFFF0F2F5),
		surfaceContainerHigh = Color(0xFFE9EDEF),
		surfaceContainerHighest = Color(0xFFE1E5E8),
	)

private val DarkColors =
	darkColorScheme(
		primary = CinelexPrimaryDark,
		onPrimary = DarkBackground,
		secondary = CinelexPrimaryDark,
		onSecondary = DarkBackground,
		background = DarkBackground,
		onBackground = DarkTextPrimary,
		surface = DarkBackground,
		onSurface = DarkTextPrimary,
		onSurfaceVariant = DarkTextSecondary,
		surfaceContainerLowest = Color(0xFF0B141A),
		surfaceContainerLow = Color(0xFF111B21),
		surfaceContainer = Color(0xFF182229),
		surfaceContainerHigh = Color(0xFF202C33),
		surfaceContainerHighest = Color(0xFF2A3942),
	)

private val ProximaNova =
	FontFamily(
		Font(R.font.proxima_nova_regular, FontWeight.Normal),
		Font(R.font.proxima_nova_medium, FontWeight.Medium),
		Font(R.font.proxima_nova_semibold, FontWeight.SemiBold),
		Font(R.font.proxima_nova_bold, FontWeight.Bold),
	)

private val CinelexTypography =
	Typography().run {
		copy(
			displayLarge = displayLarge.copy(fontFamily = ProximaNova),
			displayMedium = displayMedium.copy(fontFamily = ProximaNova),
			displaySmall = displaySmall.copy(fontFamily = ProximaNova),
			headlineLarge = headlineLarge.copy(fontFamily = ProximaNova),
			headlineMedium = headlineMedium.copy(fontFamily = ProximaNova),
			headlineSmall = headlineSmall.copy(fontFamily = ProximaNova),
			titleLarge = titleLarge.copy(fontFamily = ProximaNova),
			titleMedium = titleMedium.copy(fontFamily = ProximaNova),
			titleSmall = titleSmall.copy(fontFamily = ProximaNova),
			bodyLarge = bodyLarge.copy(fontFamily = ProximaNova),
			bodyMedium = bodyMedium.copy(fontFamily = ProximaNova),
			bodySmall = bodySmall.copy(fontFamily = ProximaNova),
			labelLarge = labelLarge.copy(fontFamily = ProximaNova),
			labelMedium = labelMedium.copy(fontFamily = ProximaNova),
			labelSmall = labelSmall.copy(fontFamily = ProximaNova),
		)
	}

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
