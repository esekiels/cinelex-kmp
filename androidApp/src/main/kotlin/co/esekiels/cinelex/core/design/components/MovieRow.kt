/*
 * Cinelex
 * MovieRow
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.model.Movie
import coil3.compose.SubcomposeAsyncImage

private val RatingColor = Color(0xFFFFC107)

@Composable
fun MovieRow(
	movie: Movie,
	source: String,
	showsDetails: Boolean,
	onMovieClick: (movie: Movie, source: String) -> Unit,
) {
	Row(
		Modifier
			.zoomBounds(ZoomKey(movie.id, source))
			.fillMaxWidth()
			.clickable(role = Role.Button) { onMovieClick(movie, source) }
			.clearAndSetSemantics {
				testTag = source
				contentDescription = movie.title
			}.padding(horizontal = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		SubcomposeAsyncImage(
			model = movie.posterUrl,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			loading = { Box(Modifier.fillMaxSize().shimmer()) },
			error = { Box(Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.5f))) },
			modifier = Modifier.width(60.dp).aspectRatio(2f / 3f).clip(RoundedCornerShape(8.dp)),
		)
		Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
			Text(
				movie.title,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Bold,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
			)
			if (showsDetails) {
				if (movie.releaseDate.isNotEmpty()) {
					Text(
						movie.releaseDate.take(4),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
					Icon(Icons.Default.Star, contentDescription = null, Modifier.size(16.dp), tint = RatingColor)
					Text(
						movie.rating,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
					)
				}
			}
		}
	}
}
