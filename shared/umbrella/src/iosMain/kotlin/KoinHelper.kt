import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.di.initKoin
import org.koin.mp.KoinPlatform

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

@Suppress("unused")
fun movieRepository(): MovieRepository = KoinPlatform.getKoin().get()
