/*
 * Cinelex
 * Shimmer
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.core.design.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.core.design.theme.CinelexTheme

private val ShimmerColors =
	listOf(
		Color.Gray.copy(alpha = 0.3f),
		Color.Gray.copy(alpha = 0.6f),
		Color.Gray.copy(alpha = 0.3f),
	)

private val ShimmerShape = RoundedCornerShape(8.dp)

private const val DURATION_MS = 1_000
private const val SWEEP_PX = 1_000f
private const val BAND_PX = 500f

fun Modifier.shimmer(): Modifier =
	composed {
		val progress =
			rememberInfiniteTransition(label = "shimmer").animateFloat(
				initialValue = 0f,
				targetValue = 1f,
				animationSpec = infiniteRepeatable(tween(DURATION_MS, easing = LinearEasing), RepeatMode.Restart),
				label = "shimmer",
			)
		drawWithCache {
			val outline = ShimmerShape.createOutline(size, layoutDirection, this)
			onDrawBehind {
				val sweep = SWEEP_PX * (progress.value * 2) - BAND_PX
				drawOutline(
					outline = outline,
					brush =
						Brush.linearGradient(
							colors = ShimmerColors,
							start = Offset(sweep - BAND_PX, sweep - BAND_PX),
							end = Offset(sweep, sweep),
						),
				)
			}
		}
	}

@Preview(showBackground = true)
@Composable
private fun ShimmerPreview() {
	CinelexTheme {
		Box(Modifier.size(200.dp, 120.dp).shimmer())
	}
}
