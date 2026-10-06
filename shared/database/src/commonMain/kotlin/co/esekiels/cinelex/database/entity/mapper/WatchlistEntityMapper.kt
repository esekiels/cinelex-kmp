/*
 * Cinelex
 * WatchlistEntityMapper
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.database.entity.mapper

import co.esekiels.cinelex.database.entity.WatchlistEntity
import co.esekiels.cinelex.model.Movie

fun Movie.toWatchlistEntity(addedAt: Long): WatchlistEntity =
	WatchlistEntity(
		id = id,
		title = title,
		posterPath = posterPath,
		backdropPath = backdropPath,
		releaseDate = releaseDate,
		voteAverage = voteAverage,
		addedAt = addedAt,
	)

fun WatchlistEntity.toDomain(): Movie =
	Movie(
		id = id,
		title = title,
		posterPath = posterPath,
		backdropPath = backdropPath,
		releaseDate = releaseDate,
		voteAverage = voteAverage,
	)
