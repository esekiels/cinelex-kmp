/*
 * Cinelex
 * DetailSections
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.esekiels.cinelex.R
import co.esekiels.cinelex.core.common.openUrl
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.components.shimmer
import co.esekiels.cinelex.model.Cast
import co.esekiels.cinelex.model.Crew
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.model.Video
import coil3.compose.AsyncImage

private const val STAR_COUNT = 10
private const val CAST_LIMIT = 10
private val StarGold = Color(0xFFFFD700)

@Composable
internal fun Sections(movie: MovieDetails) {
	Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
		AsyncImage(
			model = movie.backdropUrl,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
		)
		Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
			Info(movie)
			Text(movie.overview, style = MaterialTheme.typography.bodyLarge)
			Rating(movie)
			CastSection(movie.cast)
			CrewSection(movie)
			TrailerSection(movie.youtubeTrailers)
		}
	}
}

@Composable
private fun Info(movie: MovieDetails) {
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		Text(movie.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
		val unknown = stringResource(R.string.detail_not_available)
		val length = movie.length?.let { stringResource(R.string.detail_runtime, it.hours, it.minutes) }
		Text(
			listOf(movie.genreNames, movie.releaseYear, length).joinToString(" · ") { it ?: unknown },
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun Rating(movie: MovieDetails) {
	Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
		Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
			repeat(STAR_COUNT) { index ->
				val filled = movie.isStarFilled(index)
				Icon(
					if (filled) Icons.Filled.Star else Icons.Outlined.StarOutline,
					contentDescription = null,
					tint = if (filled) StarGold else Color.Gray,
					modifier = Modifier.size(14.dp),
				)
			}
		}
		Text(
			movie.scoreRating,
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun SectionTitle(text: String) {
	Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}

@Composable
private fun CastSection(cast: List<Cast>) {
	if (cast.isEmpty()) return
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		SectionTitle(stringResource(R.string.detail_cast))
		LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 16.dp)) {
			items(cast.take(CAST_LIMIT)) { CastCard(it) }
		}
	}
}

@Composable
private fun CastCard(member: Cast) {
	Column(
		Modifier.width(80.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		val avatar = Modifier.size(56.dp).clip(CircleShape)
		if (member.profileUrl != null) {
			AsyncImage(member.profileUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = avatar)
		} else {
			Box(avatar.background(MaterialTheme.colorScheme.surfaceContainerHigh), contentAlignment = Alignment.Center) {
				Icon(Icons.Filled.Person, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
			}
		}
		Text(
			member.name,
			style = MaterialTheme.typography.labelSmall,
			fontWeight = FontWeight.Medium,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
		)
		Text(
			member.character,
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
		)
	}
}

@Composable
private fun CrewSection(movie: MovieDetails) {
	val rows =
		listOf(
			R.string.detail_director to movie.directors,
			R.string.detail_producer to movie.producers,
			R.string.detail_writer to movie.screenwriters,
		).filter { it.second.isNotEmpty() }
	if (rows.isEmpty()) return
	Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
		SectionTitle(stringResource(R.string.detail_crew))
		rows.forEach { (label, crew) -> CrewRow(stringResource(label), crew) }
	}
}

@Composable
private fun CrewRow(
	label: String,
	crew: List<Crew>,
) {
	Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
		Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
		Text(crew.joinToString(", ") { it.name }, style = MaterialTheme.typography.bodySmall)
	}
}

@Composable
private fun TrailerSection(trailers: List<Video>) {
	if (trailers.isEmpty()) return
	val context = LocalContext.current
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		SectionTitle(stringResource(R.string.detail_trailers))
		trailers.forEach { trailer ->
			Row(
				Modifier
					.fillMaxWidth()
					.heightIn(min = 48.dp)
					.clickable(role = Role.Button) { trailer.youtubeUrl?.let { context.openUrl(it) } }
					.padding(vertical = 4.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Icon(
					Icons.Filled.PlayCircle,
					stringResource(R.string.detail_play_trailer),
					Modifier.size(28.dp),
					tint = MaterialTheme.colorScheme.primary,
				)
				Spacer(Modifier.width(8.dp))
				Text(trailer.name, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
			}
		}
	}
}

@Composable
internal fun Skeleton() {
	Column(Modifier.fillMaxSize().testTag(TestTag.DETAIL_SKELETON)) {
		Spacer(Modifier.fillMaxWidth().aspectRatio(16f / 9f).shimmer())
		Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Spacer(Modifier.size(200.dp, 24.dp).shimmer())
				Spacer(Modifier.size(260.dp, 16.dp).shimmer())
			}
			Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
				Spacer(Modifier.fillMaxWidth().height(14.dp).shimmer())
				Spacer(Modifier.fillMaxWidth().height(14.dp).shimmer())
				Spacer(Modifier.fillMaxWidth(0.7f).height(14.dp).shimmer())
			}
			Spacer(Modifier.size(180.dp, 14.dp).shimmer())
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Spacer(Modifier.size(60.dp, 20.dp).shimmer())
				Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
					repeat(5) {
						Column(
							Modifier.width(80.dp),
							horizontalAlignment = Alignment.CenterHorizontally,
							verticalArrangement = Arrangement.spacedBy(6.dp),
						) {
							Spacer(Modifier.size(56.dp).clip(CircleShape).shimmer())
							Spacer(Modifier.size(60.dp, 12.dp).shimmer())
							Spacer(Modifier.size(50.dp, 10.dp).shimmer())
						}
					}
				}
			}
		}
	}
}
