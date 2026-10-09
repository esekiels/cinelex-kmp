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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.esekiels.cinelex.R
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.components.Unavailable
import co.esekiels.cinelex.core.design.errorMessage
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.model.Cast
import co.esekiels.cinelex.model.Credits
import co.esekiels.cinelex.model.Crew
import co.esekiels.cinelex.model.Genre
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.model.Video
import co.esekiels.cinelex.model.VideoResponse
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
	movieId: Int,
	title: String,
	onBack: () -> Unit,
	viewModel: DetailViewModel = koinViewModel { parametersOf(movieId) },
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
	DetailContent(title, state, isSaved, onBack, viewModel::load, viewModel::toggleWatchlist)
}

@Composable
fun DetailContent(
	title: String,
	state: UiState<MovieDetails>,
	isSaved: Boolean,
	onBack: () -> Unit,
	onRetry: () -> Unit,
	onToggleWatchlist: () -> Unit,
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
				navigationIcon = {
					IconButton(onBack) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.action_back))
					}
				},
				actions = {
					if (state is UiState.Loaded) {
						IconButton(onToggleWatchlist, Modifier.testTag(TestTag.WATCHLIST_TOGGLE)) {
							Icon(
								if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
								stringResource(if (isSaved) R.string.watchlist_remove else R.string.watchlist_add),
							)
						}
					}
				},
			)
		},
	) { padding ->
		Box(Modifier.fillMaxSize().padding(padding)) {
			when (state) {
				is UiState.Loaded -> Sections(state.data)
				is UiState.Error ->
					Unavailable(
						Icons.Default.Warning,
						stringResource(R.string.detail_error),
						Modifier.fillMaxSize().testTag(TestTag.DETAIL_ERROR),
						stringResource(errorMessage(state.code)),
					) {
						OutlinedButton(onRetry) { Text(stringResource(R.string.action_retry)) }
					}
				UiState.Loading, UiState.Empty -> Skeleton()
			}
		}
	}
}

private val previewDetails =
	MovieDetails(
		id = 278,
		title = "The Shawshank Redemption",
		overview = "Two imprisoned men bond over a number of years.",
		voteAverage = 8.7,
		releaseDate = "1994-09-23",
		runtime = 142,
		genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
		credits =
			Credits(
				cast = listOf(Cast(504, "Tim Robbins", "Andy Dufresne"), Cast(192, "Morgan Freeman", "Red")),
				crew = listOf(Crew(4027, "Frank Darabont", "Director"), Crew(4028, "Niki Marvin", "Producer")),
			),
		videos = VideoResponse(listOf(Video("t1", "PLl99DlL6b4", "Official Trailer", "YouTube", "Trailer"))),
	)

@Preview(showBackground = true)
@Composable
private fun LoadedPreview() {
	CinelexTheme {
		DetailContent(previewDetails.title, UiState.Loaded(previewDetails), isSaved = true, {}, {}, {})
	}
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
	CinelexTheme {
		DetailContent(previewDetails.title, UiState.Loading, isSaved = false, {}, {}, {})
	}
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
	CinelexTheme {
		DetailContent(previewDetails.title, UiState.Error("E001"), isSaved = false, {}, {}, {})
	}
}
