/*
 * Cinelex
 * Carousel
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.components.ZoomKey
import co.esekiels.cinelex.core.design.components.shimmer
import co.esekiels.cinelex.core.design.components.zoomBounds
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.model.Movie
import coil3.compose.SubcomposeAsyncImage

@Composable
internal fun cardWidth(style: CarouselStyle): Dp = LocalConfiguration.current.screenWidthDp.dp * style.widthFraction

@Composable
internal fun Carousel(
	title: String,
	movies: List<Movie>,
	style: CarouselStyle,
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
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
						.zoomBounds(ZoomKey(movie.id, title))
						.width(width)
						.clickable(role = Role.Button) { onMovieClick(movie, title) }
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
internal fun Skeleton(style: CarouselStyle) {
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

@Preview(showBackground = true)
@Composable
private fun PosterCarouselPreview() {
	CinelexTheme {
		Carousel(
			"Now Playing",
			listOf(Movie(id = 278, title = "The Shawshank Redemption"), Movie(id = 238, title = "The Godfather")),
			CarouselStyle.Poster,
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun BackdropCarouselPreview() {
	CinelexTheme {
		Carousel(
			"Popular",
			listOf(Movie(id = 278, title = "The Shawshank Redemption"), Movie(id = 238, title = "The Godfather")),
			CarouselStyle.Backdrop,
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun SkeletonPreview() {
	CinelexTheme {
		Skeleton(CarouselStyle.Poster)
	}
}
