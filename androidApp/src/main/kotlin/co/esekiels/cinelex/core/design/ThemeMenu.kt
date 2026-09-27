/*
 * Cinelex
 * ThemeMenu
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.R
import co.esekiels.cinelex.model.UiTheme

@Composable
fun ThemeMenu(
	current: UiTheme,
	onSelect: (UiTheme) -> Unit,
) {
	var showPicker by remember { mutableStateOf(false) }
	val options =
		listOf(
			UiTheme.FOLLOW_SYSTEM to stringResource(R.string.theme_system),
			UiTheme.LIGHT to stringResource(R.string.theme_light),
			UiTheme.DARK to stringResource(R.string.theme_dark),
		)
	val icon =
		when (current) {
			UiTheme.LIGHT -> Icons.Default.LightMode
			UiTheme.DARK -> Icons.Default.DarkMode
			UiTheme.FOLLOW_SYSTEM -> Icons.Default.BrightnessAuto
		}
	val currentLabel = options.first { it.first == current }.second
	IconButton(
		onClick = { showPicker = true },
		modifier = Modifier.semantics { stateDescription = currentLabel },
	) {
		Icon(icon, contentDescription = stringResource(R.string.action_theme))
	}
	if (showPicker) {
		ThemePickerDialog(
			options = options,
			current = current,
			onSelect = onSelect,
			onDismiss = { showPicker = false },
		)
	}
}

@Composable
private fun ThemePickerDialog(
	options: List<Pair<UiTheme, String>>,
	current: UiTheme,
	onSelect: (UiTheme) -> Unit,
	onDismiss: () -> Unit,
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text(stringResource(R.string.picker_theme_title)) },
		text = {
			Column {
				options.forEach { (theme, label) ->
					val select = {
						onDismiss()
						onSelect(theme)
					}
					Row(
						modifier = Modifier.fillMaxWidth().clickable(onClick = select).padding(vertical = 4.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						RadioButton(selected = theme == current, onClick = select)
						Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
					}
				}
			}
		},
		confirmButton = {
			TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_ok)) }
		},
	)
}
