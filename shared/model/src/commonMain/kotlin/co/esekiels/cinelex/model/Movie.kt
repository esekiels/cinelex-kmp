/*
 * Cinelex
 * Movie
 *
 * Created by Esekiel Surbakti on 18/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
	val id: Int,
	val title: String,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("release_date")
	val releaseDate: String = "",
	@SerialName("vote_average")
	val voteAverage: Double = 0.0,
) {
	val posterUrl: String? get() = tmdbImageUrl(posterPath, POSTER_SIZE)
	val backdropUrl: String? get() = tmdbImageUrl(backdropPath, BACKDROP_SIZE)
	val rating: String get() = voteAverage.toOneDecimal()
}

internal const val POSTER_SIZE = "w342"
internal const val BACKDROP_SIZE = "w780"
internal const val PROFILE_SIZE = "w185"

internal fun tmdbImageUrl(
	path: String?,
	size: String,
): String? = path?.let { "https://image.tmdb.org/t/p/$size$it" }
