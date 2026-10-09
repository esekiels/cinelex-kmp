/*
 * Cinelex
 * WatchlistScreen
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.esekiels.cinelex.R
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.components.MovieRow
import co.esekiels.cinelex.core.design.components.Unavailable
import co.esekiels.cinelex.core.design.errorMessage
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.model.Movie
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistScreen(
	viewModel: WatchlistViewModel = koinViewModel(),
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	WatchlistContent(state, onMovieClick)
}

@Composable
fun WatchlistContent(
	state: UiState<List<Movie>>,
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
) {
	Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.watchlist)) }) }) { padding ->
		val modifier = Modifier.fillMaxSize().padding(padding)
		when (state) {
			is UiState.Loaded ->
				LazyColumn(
					modifier,
					contentPadding = PaddingValues(vertical = 8.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp),
				) {
					items(state.data, key = { it.id }) { movie ->
						MovieRow(movie, TestTag.WATCHLIST_ROW, showsDetails = true, onMovieClick)
					}
				}

			UiState.Empty ->
				Unavailable(
					Icons.Default.BookmarkBorder,
					stringResource(R.string.watchlist_empty),
					modifier.testTag(TestTag.WATCHLIST_EMPTY),
					stringResource(R.string.watchlist_empty_message),
				)

			is UiState.Error ->
				Unavailable(
					Icons.Default.Warning,
					stringResource(R.string.home_error),
					modifier,
					stringResource(errorMessage(state.code)),
				)

			UiState.Loading -> Unit
		}
	}
}

private val previewMovies =
	listOf(
		Movie(id = 278, title = "The Shawshank Redemption", releaseDate = "1994-09-23", voteAverage = 8.7),
		Movie(id = 238, title = "The Godfather", releaseDate = "1972-03-14", voteAverage = 8.7),
	)

@Preview(showBackground = true)
@Composable
private fun LoadedPreview() {
	CinelexTheme {
		WatchlistContent(UiState.Loaded(previewMovies))
	}
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
	CinelexTheme {
		WatchlistContent(UiState.Empty)
	}
}
