/*
 * Cinelex
 * Credits
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credits(
	val cast: List<Cast> = emptyList(),
	val crew: List<Crew> = emptyList(),
)

@Serializable
data class Cast(
	val id: Int,
	val name: String,
	val character: String = "",
	@SerialName("profile_path")
	val profilePath: String? = null,
) {
	val profileUrl: String? get() = tmdbImageUrl(profilePath, PROFILE_SIZE)
}

@Serializable
data class Crew(
	val id: Int,
	val name: String,
	val job: String,
)
