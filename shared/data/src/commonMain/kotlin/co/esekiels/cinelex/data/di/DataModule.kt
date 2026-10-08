/*
 * Cinelex
 * DataModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data.di

import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.MovieRepositoryImpl
import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.data.UserDataRepositoryImpl
import co.esekiels.cinelex.data.WatchlistRepository
import co.esekiels.cinelex.data.WatchlistRepositoryImpl
import co.esekiels.cinelex.data.contentLanguage
import kotlinx.coroutines.flow.map
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule: Module =
	module {
		single {
			MovieRepositoryImpl(
				service = get(),
				dao = get(),
				language = get<UserDataRepository>().userPreferences.map { it.contentLanguage() },
			)
		} bind MovieRepository::class
		single { WatchlistRepositoryImpl(dao = get()) } bind WatchlistRepository::class
		single { UserDataRepositoryImpl(dataSource = get()) } bind UserDataRepository::class
	}
