import co.esekiels.cinelex.di.initKoin

/*
 * Cinelex
 * KoinHelper
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

/**
 * Swift cannot see kotlin default arguments, so initKoin()
 * would arrive requiring both params
 */
@Suppress("unused")
fun doInitKoin() {
	initKoin()
}
