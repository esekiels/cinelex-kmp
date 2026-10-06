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
	val backdropUrl: String? get() = tmdbImageUrl(backdropPath, BACKDROP_SIZE)

	val genreNames: String? get() = genres.takeIf { it.isNotEmpty() }?.joinToString(", ") { it.name }

	val releaseYear: String? get() = releaseDate.takeIf { it.isNotEmpty() }?.take(YEAR_LENGTH)

	val length: Length? get() = runtime?.let { Length(it / MINUTES_PER_HOUR, it % MINUTES_PER_HOUR) }

	val scoreRating: String get() = voteAverage.toOneDecimal()

	fun isStarFilled(index: Int): Boolean = index < voteAverage.roundToInt()

	val cast: List<Cast> get() = credits.cast

	val directors: List<Crew> get() = credits.crew.filter { it.job == "Director" }

	val producers: List<Crew> get() = credits.crew.filter { it.job == "Producer" }

	val screenwriters: List<Crew> get() = credits.crew.filter { it.job == "Screenplay" || it.job == "Writer" }

	val youtubeTrailers: List<Video> get() = videos.results.filter { it.site == "YouTube" && it.type == "Trailer" }

	fun toMovie(): Movie =
		Movie(
			id = id,
			title = title,
			backdropPath = backdropPath,
			posterPath = posterPath,
			releaseDate = releaseDate,
			voteAverage = voteAverage,
		)
}

data class Length(
	val hours: Int,
	val minutes: Int,
)
