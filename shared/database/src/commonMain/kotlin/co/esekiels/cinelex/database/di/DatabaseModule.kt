/*
 * Cinelex
 * DatabaseModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.database.di

import co.esekiels.cinelex.database.CinelexDatabase
import co.esekiels.cinelex.database.buildDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databasePlatformModule: Module

val databaseModule: Module =
	module {
		includes(databasePlatformModule)

		single<CinelexDatabase> { buildDatabase(get()) }
		single { get<CinelexDatabase>().movieDao() }
	}
