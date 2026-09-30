/*
 * Cinelex
 * MovieEntityMapper
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.database.entity.mapper

import co.esekiels.cinelex.database.entity.MovieDetailsEntity
import co.esekiels.cinelex.database.entity.MovieEntity
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import kotlinx.serialization.json.Json

fun List<Movie>.toEntities(
	category: String,
	language: String,
): List<MovieEntity> =
	mapIndexed { position, movie ->
		MovieEntity(
			id = movie.id,
			title = movie.title,
			posterPath = movie.posterPath ?: "",
			backdropPath = movie.backdropPath ?: "",
			category = category,
			position = position,
			language = language,
		)
	}

fun List<MovieEntity>.toDomain(): List<Movie> =
	map { entity ->
		Movie(
			id = entity.id,
			title = entity.title,
			posterPath = entity.posterPath.ifEmpty { null },
			backdropPath = entity.backdropPath.ifEmpty { null },
		)
	}

private val cacheJson = Json { ignoreUnknownKeys = true }

fun MovieDetails.toEntity(language: String): MovieDetailsEntity =
	MovieDetailsEntity(id = id, language = language, json = cacheJson.encodeToString(this))

@Suppress("SwallowedException")
fun MovieDetailsEntity.toDomainOrNull(): MovieDetails? =
	try {
		cacheJson.decodeFromString<MovieDetails>(json)
	} catch (_: IllegalArgumentException) {
		null
	}
