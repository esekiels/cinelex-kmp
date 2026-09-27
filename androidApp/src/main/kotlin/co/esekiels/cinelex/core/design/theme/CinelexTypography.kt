/*
 * Cinelex
 * CinelexTypography
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import co.esekiels.cinelex.R

private val ProximaNova =
	FontFamily(
		Font(R.font.proxima_nova_regular, FontWeight.Normal),
		Font(R.font.proxima_nova_medium, FontWeight.Medium),
		Font(R.font.proxima_nova_semibold, FontWeight.SemiBold),
		Font(R.font.proxima_nova_bold, FontWeight.Bold),
	)

internal val CinelexTypography =
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
