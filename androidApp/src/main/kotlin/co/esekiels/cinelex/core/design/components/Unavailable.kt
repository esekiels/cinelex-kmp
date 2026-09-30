/*
 * Cinelex
 * Unavailable
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.core.design.theme.CinelexTheme

@Composable
fun Unavailable(
	icon: ImageVector,
	title: String,
	modifier: Modifier = Modifier,
	message: String? = null,
	action: (@Composable () -> Unit)? = null,
) {
	Column(
		modifier.padding(horizontal = 32.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
	) {
		Icon(icon, contentDescription = null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
		Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
		message?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
		action?.invoke()
	}
}

@Preview(showBackground = true)
@Composable
private fun UnavailablePreview() {
	CinelexTheme {
		Unavailable(Icons.Default.Warning, "Something went wrong", message = "Server unreachable")
	}
}
