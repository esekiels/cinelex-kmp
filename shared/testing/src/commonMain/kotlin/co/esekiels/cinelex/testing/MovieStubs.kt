/*
 * Cinelex
 * MovieStubs
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.model.Movie

object MovieStubs {
	val shawshank =
		Movie(
			id = 278,
			title = "The Shawshank Redemption",
			backdropPath = "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
			posterPath = "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
			releaseDate = "1994-09-23",
			voteAverage = 8.7,
			voteCount = 28_000,
			genreIds = listOf(18, 80),
		)

	val godfather =
		Movie(
			id = 238,
			title = "The Godfather",
			backdropPath = "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
			posterPath = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
			releaseDate = "1972-03-14",
			voteAverage = 8.7,
			voteCount = 21_000,
			genreIds = listOf(18, 80),
		)

	val all: List<Movie> = listOf(shawshank, godfather)
}
