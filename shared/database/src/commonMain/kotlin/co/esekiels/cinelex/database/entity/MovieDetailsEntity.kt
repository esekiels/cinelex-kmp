/*
 * Cinelex
 * MovieDetailsEntity
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "language"])
data class MovieDetailsEntity(
	val id: Int,
	val language: String,
	val json: String,
)
