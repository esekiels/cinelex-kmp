/*
 * Cinelex
 * MovieStubs
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.model.Cast
import co.esekiels.cinelex.model.Credits
import co.esekiels.cinelex.model.Crew
import co.esekiels.cinelex.model.Genre
import co.esekiels.cinelex.model.Movie
import co.esekiels.cinelex.model.MovieDetails
import co.esekiels.cinelex.model.Video
import co.esekiels.cinelex.model.VideoResponse

object MovieStubs {
	val shawshank =
		Movie(
			id = 278,
			title = "The Shawshank Redemption",
			backdropPath = "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
			posterPath = "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
			releaseDate = "1994-09-23",
			voteAverage = 8.7,
		)

	val godfather =
		Movie(
			id = 238,
			title = "The Godfather",
			backdropPath = "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
			posterPath = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
			releaseDate = "1972-03-14",
			voteAverage = 8.7,
		)

	val all: List<Movie> = listOf(shawshank, godfather)

	@Suppress("MagicNumber")
	fun details(id: Int): MovieDetails {
		val movie = all.first { it.id == id }
		return MovieDetails(
			id = movie.id,
			title = movie.title,
			backdropPath = movie.backdropPath,
			posterPath = movie.posterPath,
			overview = "Two imprisoned men bond over a number of years.",
			voteAverage = movie.voteAverage,
			releaseDate = movie.releaseDate,
			runtime = 142,
			genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
			credits =
				Credits(
					cast =
						listOf(
							Cast(504, "Tim Robbins", "Andy Dufresne"),
							Cast(192, "Morgan Freeman", "Ellis Boyd 'Red' Redding"),
						),
					crew =
						listOf(
							Crew(4027, "Frank Darabont", "Director"),
							Crew(4028, "Niki Marvin", "Producer"),
							Crew(4029, "Stephen King", "Writer"),
						),
				),
			videos = VideoResponse(listOf(Video("t1", "PLl99DlL6b4", "Official Trailer", "YouTube", "Trailer"))),
		)
	}
}
