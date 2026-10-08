/*
 * Cinelex
 * MovieResponse
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.model

import co.esekiels.cinelex.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
	@SerialName("total_pages")
	val totalPages: Int,
	val results: List<Movie>,
)
