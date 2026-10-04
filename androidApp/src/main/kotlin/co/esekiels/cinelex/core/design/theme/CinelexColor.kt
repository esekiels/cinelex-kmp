/*
 * Cinelex
 * CinelexColor
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val CinelexPrimary = Color(0xFF075E54)
private val CinelexPrimaryDark = Color(0xFF00A884)

private val DarkBackground = Color(0xFF111B21)
private val DarkTextPrimary = Color(0xFFE9EDEF)
private val DarkTextSecondary = Color(0xFF8696A0)

private val LightBackground = Color(0xFFFFFFFF)
private val LightTextPrimary = Color(0xFF111B21)
private val LightTextSecondary = Color(0xFF667781)

internal val LightColors =
	lightColorScheme(
		primary = CinelexPrimary,
		onPrimary = Color.White,
		secondary = CinelexPrimary,
		onSecondary = Color.White,
		secondaryContainer = Color(0xFFD9FDD3),
		onSecondaryContainer = CinelexPrimary,
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

internal val DarkColors =
	darkColorScheme(
		primary = CinelexPrimaryDark,
		onPrimary = DarkBackground,
		secondary = CinelexPrimaryDark,
		onSecondary = DarkBackground,
		secondaryContainer = Color(0xFF103529),
		onSecondaryContainer = Color(0xFFD9FDD3),
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
