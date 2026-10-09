/*
 * Cinelex
 * ZoomKey
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.core.design.components

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

data class ZoomKey<T>(
	val value: T,
	val source: String,
)

private val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

@Composable
fun ZoomTransitionLayout(content: @Composable () -> Unit) {
	SharedTransitionLayout {
		CompositionLocalProvider(LocalSharedTransitionScope provides this) { content() }
	}
}

@Composable
fun Modifier.zoomBounds(key: ZoomKey<*>): Modifier {
	val scope = LocalSharedTransitionScope.current ?: return this
	return with(scope) {
		sharedBounds(
			rememberSharedContentState(key),
			LocalNavAnimatedContentScope.current,
			resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
		)
	}
}
