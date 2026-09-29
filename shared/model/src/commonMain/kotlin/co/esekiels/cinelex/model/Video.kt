/*
 * Cinelex
 * Video
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
	val results: List<Video> = emptyList(),
)

@Serializable
data class Video(
	val id: String,
	val key: String,
	val name: String,
	val site: String,
	val type: String,
) {
	val youtubeUrl: String? get() = if (site == "YouTube") "https://www.youtube.com/watch?v=$key" else null
}
