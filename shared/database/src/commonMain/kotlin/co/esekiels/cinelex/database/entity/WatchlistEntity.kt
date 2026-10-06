/*
 * Cinelex
 * WatchlistEntity
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** A snapshot of the movie at save time, in the language it was saved in. */
@Entity
data class WatchlistEntity(
	@PrimaryKey val id: Int,
	val title: String,
	@ColumnInfo("poster_path")
	val posterPath: String?,
	@ColumnInfo("backdrop_path")
	val backdropPath: String?,
	@ColumnInfo("release_date")
	val releaseDate: String,
	@ColumnInfo("vote_average")
	val voteAverage: Double,
	@ColumnInfo("added_at")
	val addedAt: Long,
)
