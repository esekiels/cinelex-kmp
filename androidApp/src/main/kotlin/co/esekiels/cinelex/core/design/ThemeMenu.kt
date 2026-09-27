/*
 * Cinelex
 * ThemeMenu
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import co.esekiels.cinelex.R

private const val THEME_KEY = "theme"

private fun Context.settings() = getSharedPreferences("settings", Context.MODE_PRIVATE)

fun Context.applySavedTheme() {
	AppCompatDelegate.setDefaultNightMode(settings().getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))
}

@Composable
fun ThemeMenu() {
	val context = LocalContext.current
	var mode by remember { mutableIntStateOf(AppCompatDelegate.getDefaultNightMode()) }
	var showPicker by remember { mutableStateOf(false) }
	val options =
		listOf(
			AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to stringResource(R.string.theme_system),
			AppCompatDelegate.MODE_NIGHT_NO to stringResource(R.string.theme_light),
			AppCompatDelegate.MODE_NIGHT_YES to stringResource(R.string.theme_dark),
		)
	val icon =
		when (mode) {
			AppCompatDelegate.MODE_NIGHT_NO -> Icons.Default.LightMode
			AppCompatDelegate.MODE_NIGHT_YES -> Icons.Default.DarkMode
			else -> Icons.Default.BrightnessAuto
		}
	val currentLabel = options.firstOrNull { it.first == mode }?.second ?: options.first().second
	IconButton(
		onClick = { showPicker = true },
		modifier = Modifier.semantics { stateDescription = currentLabel },
	) {
		Icon(icon, contentDescription = stringResource(R.string.action_theme))
	}
	if (showPicker) {
		ThemePickerDialog(
			options = options,
			current = mode,
			onSelect = {
				mode = it
				context.settings().edit { putInt(THEME_KEY, it) }
				AppCompatDelegate.setDefaultNightMode(it)
			},
			onDismiss = { showPicker = false },
		)
	}
}

@Composable
private fun ThemePickerDialog(
	options: List<Pair<Int, String>>,
	current: Int,
	onSelect: (Int) -> Unit,
	onDismiss: () -> Unit,
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text(stringResource(R.string.picker_theme_title)) },
		text = {
			Column {
				options.forEach { (mode, label) ->
					val select = {
						onDismiss()
						onSelect(mode)
					}
					Row(
						modifier = Modifier.fillMaxWidth().clickable(onClick = select).padding(vertical = 4.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						RadioButton(selected = mode == current, onClick = select)
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
