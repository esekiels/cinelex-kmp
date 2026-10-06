import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.data.WatchlistRepository
import co.esekiels.cinelex.data.di.initKoin
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

@Suppress("unused")
fun userDataRepository(): UserDataRepository = KoinPlatform.getKoin().get()

@Suppress("unused")
fun watchlistRepository(): WatchlistRepository = KoinPlatform.getKoin().get()

@Suppress("unused")
fun currentUserPreferences(repo: UserDataRepository): UserPreferences = runBlocking { repo.userPreferences.first() }
