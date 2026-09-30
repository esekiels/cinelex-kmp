/*
 * Cinelex
 * MovieEntity
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id", "category", "language"])
data class MovieEntity(
	val id: Int,
	val title: String,
	@ColumnInfo("poster_path")
	val posterPath: String,
	@ColumnInfo("backdrop_path")
	val backdropPath: String,
	val category: String,
	val position: Int,
	val language: String,
)
