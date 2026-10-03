/*
 * Cinelex
 * TestTag
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

/** Mirrored by hand in benchmark/Common.kt — update both when renaming. */
object TestTag {
	const val HOME_LIST = "HomeList"
	const val MOVIE_CARD = "MovieCard"
	const val HOME_SKELETON = "HomeSkeleton"
	const val HOME_EMPTY = "HomeEmpty"
	const val HOME_ERROR = "HomeError"
	const val DETAIL_CONTENT = "DetailContent"
	const val DETAIL_SKELETON = "DetailSkeleton"
	const val DETAIL_ERROR = "DetailError"

	/** Every carousel is fed the same movies, so a card lookup has to be scoped to one section. */
	fun carousel(title: String) = "Carousel-$title"
}
