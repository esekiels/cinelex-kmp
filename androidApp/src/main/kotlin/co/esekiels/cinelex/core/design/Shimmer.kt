/*
 * Cinelex
 * Shimmer
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.core.design

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ShimmerColors =
	listOf(
		Color.Gray.copy(alpha = 0.3f),
		Color.Gray.copy(alpha = 0.6f),
		Color.Gray.copy(alpha = 0.3f),
	)

private const val DURATION_MS = 1_000
private const val SWEEP_PX = 1_000f
private const val BAND_PX = 500f

/** Rounded gray block with a diagonal sweep — the Android twin of iOS `shimmerEffect()`. */
fun Modifier.shimmer(): Modifier =
	composed {
		val progress by rememberInfiniteTransition(label = "shimmer").animateFloat(
			initialValue = 0f,
			targetValue = 1f,
			animationSpec = infiniteRepeatable(tween(DURATION_MS, easing = LinearEasing), RepeatMode.Restart),
			label = "shimmer",
		)
		val sweep = SWEEP_PX * (progress * 2) - BAND_PX
		background(
			brush =
				Brush.linearGradient(
					colors = ShimmerColors,
					start = Offset(sweep - BAND_PX, sweep - BAND_PX),
					end = Offset(sweep, sweep),
				),
			shape = RoundedCornerShape(8.dp),
		)
	}
