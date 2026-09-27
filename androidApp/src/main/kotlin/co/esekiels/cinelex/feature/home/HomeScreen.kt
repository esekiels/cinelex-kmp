/*
 * Cinelex
 * HomeScreen
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.shimmer
import co.esekiels.cinelex.model.Movie
import coil3.compose.SubcomposeAsyncImage

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
	HomeContent(state, isRefreshing, viewModel::refresh)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
	state: UiState<Carousels>,
	isRefreshing: Boolean,
	onRefresh: () -> Unit,
) {
	Scaffold { padding ->
		PullToRefreshBox(isRefreshing, onRefresh, Modifier.padding(padding)) {
			LazyColumn(
				modifier = Modifier.fillMaxSize().testTag(TestTag.HOME_LIST),
				contentPadding = PaddingValues(vertical = 16.dp),
				verticalArrangement = Arrangement.spacedBy(24.dp),
			) {
				when (state) {
					is UiState.Loaded ->
						with(state.data) {
							item { Carousel("Now Playing", nowPlaying, CarouselStyle.Poster) }
							item { Carousel("Popular", popular, CarouselStyle.Backdrop) }
							item { Carousel("Top Rated", topRated, CarouselStyle.Poster) }
							item { Carousel("Upcoming", upcoming, CarouselStyle.Backdrop) }
						}

					UiState.Loading ->
						items(listOf(CarouselStyle.Poster, CarouselStyle.Backdrop, CarouselStyle.Poster, CarouselStyle.Backdrop)) {
							Skeleton(it)
						}

					UiState.Empty ->
						item {
							Unavailable(Icons.Default.Info, "No movies", Modifier.fillParentMaxSize().testTag(TestTag.HOME_EMPTY))
						}

					is UiState.Error ->
						item {
							Unavailable(
								Icons.Default.Warning,
								"Something went wrong",
								Modifier.fillParentMaxSize().testTag(TestTag.HOME_ERROR),
								state.message,
							)
						}
				}
			}
		}
	}
}

@Composable
private fun cardWidth(style: CarouselStyle): Dp = LocalConfiguration.current.screenWidthDp.dp * style.widthFraction

@Composable
private fun Carousel(
	title: String,
	movies: List<Movie>,
	style: CarouselStyle,
) {
	val width = cardWidth(style)
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		Text(
			title,
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			modifier = Modifier.padding(horizontal = 16.dp),
		)
		LazyRow(
			modifier = Modifier.testTag(TestTag.carousel(title)),
			contentPadding = PaddingValues(horizontal = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(if (style == CarouselStyle.Poster) 16.dp else 8.dp),
		) {
			items(movies, key = { it.id }) { movie ->
				Column(
					Modifier
						.width(width)
						// On the card, not the image: Coil drops the image's description while loading or on error.
						// Clearing children keeps the Backdrop title Text from being announced twice.
						.clearAndSetSemantics {
							testTag = TestTag.MOVIE_CARD
							contentDescription = movie.title
						},
					verticalArrangement = Arrangement.spacedBy(8.dp),
				) {
					SubcomposeAsyncImage(
						model = if (style == CarouselStyle.Poster) movie.posterUrl else movie.backdropUrl,
						contentDescription = null,
						contentScale = ContentScale.Crop,
						loading = { Box(Modifier.fillMaxSize().shimmer()) },
						error = { Box(Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.5f))) },
						modifier =
							Modifier
								.aspectRatio(style.aspectRatio)
								.clip(RoundedCornerShape(8.dp)),
					)
					if (style == CarouselStyle.Backdrop) {
						Text(
							movie.title,
							style = MaterialTheme.typography.titleSmall,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
						)
					}
				}
			}
		}
	}
}

@Composable
private fun Skeleton(style: CarouselStyle) {
	val width = cardWidth(style)
	Column(Modifier.testTag(TestTag.HOME_SKELETON), verticalArrangement = Arrangement.spacedBy(12.dp)) {
		Spacer(Modifier.padding(start = 16.dp).size(150.dp, 30.dp).shimmer())
		LazyRow(
			contentPadding = PaddingValues(horizontal = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			userScrollEnabled = false,
		) {
			items(5) { Spacer(Modifier.width(width).aspectRatio(style.aspectRatio).shimmer()) }
		}
	}
}

@Composable
private fun Unavailable(
	icon: ImageVector,
	title: String,
	modifier: Modifier = Modifier,
	message: String? = null,
) {
	Column(
		modifier.padding(horizontal = 32.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
	) {
		Icon(icon, contentDescription = null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
		Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
		message?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) }
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
private fun LoadedPreview() = HomeContent(UiState.Loaded(previewCarousels), false) {}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() = HomeContent(UiState.Loading, false) {}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() = HomeContent(UiState.Empty, false) {}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() = HomeContent(UiState.Error("E001", "Server unreachable"), false) {}
