/*
 * Cinelex
 * LanguageMenu
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import co.esekiels.cinelex.R
import java.util.Locale

@Composable
fun LanguageMenu() {
	var showPicker by remember { mutableStateOf(false) }
	IconButton(onClick = { showPicker = true }) {
		Icon(Icons.Default.Language, contentDescription = stringResource(R.string.action_language))
	}
	if (showPicker) {
		LanguagePickerDialog(onDismiss = { showPicker = false })
	}
}

@Composable
private fun LanguagePickerDialog(onDismiss: () -> Unit) {
	val current = LocalConfiguration.current.locales[0].language
	val options =
		listOf(
			"en" to stringResource(R.string.language_english),
			"id" to stringResource(R.string.language_indonesian),
		)
	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text(stringResource(R.string.picker_language_title)) },
		text = {
			Column {
				options.forEach { (tag, label) ->
					val select = {
						onDismiss()
						AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
					}
					Row(
						modifier = Modifier.fillMaxWidth().clickable(onClick = select).padding(vertical = 4.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						RadioButton(selected = Locale.forLanguageTag(tag).language == current, onClick = select)
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
