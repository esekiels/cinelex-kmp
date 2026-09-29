/*
 * Cinelex
 * Genre
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
	val id: Int,
	val name: String,
)
