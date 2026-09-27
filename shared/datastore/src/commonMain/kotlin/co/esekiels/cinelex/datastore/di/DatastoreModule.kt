/*
 * Cinelex
 * DatastoreModule
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.datastore.di

import co.esekiels.cinelex.datastore.UserPreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val datastorePlatformModule: Module

val datastoreModule: Module =
	module {
		includes(datastorePlatformModule)
		singleOf(::UserPreferencesDataSource)
	}
