/*
 * Cinelex
 * MovieDetails
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

private const val MINUTES_PER_HOUR = 60
private const val YEAR_LENGTH = 4

@Serializable
data class MovieDetails(
	val id: Int,
	val title: String,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	val overview: String = "",
	@SerialName("vote_average")
	val voteAverage: Double = 0.0,
	@SerialName("release_date")
	val releaseDate: String = "",
	val runtime: Int? = null,
	val genres: List<Genre> = emptyList(),
	val credits: Credits = Credits(),
	val videos: VideoResponse = VideoResponse(),
) {
	val backdropUrl: String? get() = backdropPath?.let { "$IMAGE_BASE_URL$it" }

	val genreFormatted: String get() = genres.takeIf { it.isNotEmpty() }?.joinToString(", ") { it.name } ?: "N/A"

	val releaseYearFormatted: String get() = releaseDate.takeIf { it.isNotEmpty() }?.take(YEAR_LENGTH) ?: "N/A"

	val durationFormatted: String
		get() = runtime?.let { "${it / MINUTES_PER_HOUR}h ${it % MINUTES_PER_HOUR}m" } ?: "N/A"

	val scoreRating: String get() = voteAverage.toOneDecimal()

	fun isStarFilled(index: Int): Boolean = index < voteAverage.roundToInt()

	val cast: List<Cast> get() = credits.cast

	val directors: List<Crew> get() = credits.crew.filter { it.job == "Director" }

	val producers: List<Crew> get() = credits.crew.filter { it.job == "Producer" }

	val screenwriters: List<Crew> get() = credits.crew.filter { it.job == "Screenplay" || it.job == "Writer" }

	val youtubeTrailers: List<Video> get() = videos.results.filter { it.site == "YouTube" && it.type == "Trailer" }
}
