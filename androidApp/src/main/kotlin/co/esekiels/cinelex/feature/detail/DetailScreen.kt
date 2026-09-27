/*
 * Cinelex
 * DetailScreen
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.esekiels.cinelex.R
import co.esekiels.cinelex.core.design.theme.CinelexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
	title: String,
	onBack: () -> Unit,
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(title) },
				navigationIcon = {
					IconButton(onBack) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.action_back))
					}
				},
			)
		},
	) { padding ->
		Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
			Text(stringResource(R.string.detail_placeholder))
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
	CinelexTheme {
		DetailScreen("The Godfather") {}
	}
}
