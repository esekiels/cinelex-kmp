/*
 * Cinelex
 * HomeScreen
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.feature.home

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import co.esekiels.cinelex.core.design.components.Unavailable
import co.esekiels.cinelex.core.design.errorMessage
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.model.Movie
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
	viewModel: HomeViewModel = koinViewModel(),
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
	actions: @Composable RowScope.() -> Unit,
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
	ReportDrawnWhen { state !is UiState.Loading }
	HomeContent(state, isRefreshing, actions, onMovieClick, viewModel::refresh)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
	state: UiState<Carousels>,
	isRefreshing: Boolean,
	actions: @Composable RowScope.() -> Unit = {},
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
	onRefresh: () -> Unit,
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(R.string.app_name)) },
				actions = actions,
				colors = TopAppBarDefaults.topAppBarColors(actionIconContentColor = MaterialTheme.colorScheme.onSurface),
			)
		},
	) { padding ->
		PullToRefreshBox(isRefreshing, onRefresh, Modifier.padding(padding)) {
			LazyColumn(
				modifier = Modifier.fillMaxSize().testTag(TestTag.HOME_LIST),
				contentPadding = PaddingValues(vertical = 16.dp),
				verticalArrangement = Arrangement.spacedBy(24.dp),
			) {
				when (state) {
					is UiState.Loaded ->
						with(state.data) {
							item { Carousel(stringResource(R.string.home_now_playing), nowPlaying, CarouselStyle.Poster, onMovieClick) }
							item { Carousel(stringResource(R.string.home_popular), popular, CarouselStyle.Backdrop, onMovieClick) }
							item { Carousel(stringResource(R.string.home_top_rated), topRated, CarouselStyle.Poster, onMovieClick) }
							item { Carousel(stringResource(R.string.home_upcoming), upcoming, CarouselStyle.Backdrop, onMovieClick) }
						}

					UiState.Loading ->
						items(listOf(CarouselStyle.Poster, CarouselStyle.Backdrop, CarouselStyle.Poster, CarouselStyle.Backdrop)) {
							Skeleton(it)
						}

					UiState.Empty ->
						item {
							Unavailable(
								Icons.Default.Info,
								stringResource(R.string.home_empty),
								Modifier.fillParentMaxSize().testTag(TestTag.HOME_EMPTY),
							)
						}

					is UiState.Error ->
						item {
							Unavailable(
								Icons.Default.Warning,
								stringResource(R.string.home_error),
								Modifier.fillParentMaxSize().testTag(TestTag.HOME_ERROR),
								stringResource(errorMessage(state.code)),
							)
						}
				}
			}
		}
	}
}

private val previewMovies =
	listOf(
		Movie(id = 278, title = "The Shawshank Redemption"),
		Movie(id = 238, title = "The Godfather"),
	)

private val previewCarousels = Carousels(previewMovies, previewMovies, previewMovies, previewMovies)

@Preview(showBackground = true)
@Composable
private fun LoadedPreview() {
	CinelexTheme {
		HomeContent(UiState.Loaded(previewCarousels), false) {}
	}
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
	CinelexTheme {
		HomeContent(UiState.Loading, false) {}
	}
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
	CinelexTheme {
		HomeContent(UiState.Empty, false) {}
	}
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
	CinelexTheme {
		HomeContent(UiState.Error("E001"), false) {}
	}
}
