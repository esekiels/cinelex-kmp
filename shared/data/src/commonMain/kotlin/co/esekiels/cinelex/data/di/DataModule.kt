/*
 * Cinelex
 * DataModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data.di

import co.esekiels.cinelex.common.di.IO_DISPATCHER
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.data.MovieRepositoryImpl
import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.data.UserDataRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule: Module =
	module {
		single {
			MovieRepositoryImpl(
				client = get(),
				dao = get(),
				ioDispatcher = get(IO_DISPATCHER),
			)
		} bind MovieRepository::class
		single { UserDataRepositoryImpl(dataSource = get()) } bind UserDataRepository::class
	}
