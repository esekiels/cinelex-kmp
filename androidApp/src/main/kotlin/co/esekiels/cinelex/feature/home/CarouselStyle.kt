/*
 * Cinelex
 * CarouselStyle
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.feature.home

private const val POSTER_WIDTH = 0.4f
private const val POSTER_RATIO = 2f / 3f
private const val BACKDROP_WIDTH = 0.75f
private const val BACKDROP_RATIO = 16f / 9f

/** Card width is a fraction of the screen width, height follows from the image's aspect ratio. */
enum class CarouselStyle(
	val widthFraction: Float,
	val aspectRatio: Float,
) {
	Poster(POSTER_WIDTH, POSTER_RATIO),
	Backdrop(BACKDROP_WIDTH, BACKDROP_RATIO),
}
