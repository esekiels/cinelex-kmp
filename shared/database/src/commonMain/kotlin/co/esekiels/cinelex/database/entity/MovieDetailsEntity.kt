/*
 * Cinelex
 * MovieDetailsEntity
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailsEntity(
	@PrimaryKey val id: Int,
	val json: String,
)
