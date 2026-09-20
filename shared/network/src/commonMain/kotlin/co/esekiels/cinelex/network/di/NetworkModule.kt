/*
 * Cinelex
 * NetworkModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.network.di

import co.esekiels.cinelex.network.cinelexJson
import co.esekiels.cinelex.network.createHttpClient
import co.esekiels.cinelex.network.service.MovieClient
import co.esekiels.cinelex.network.service.MovieService
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun networkModule(enableLogging: Boolean): Module =
	module {
		single { cinelexJson() }

		single<HttpClient> { createHttpClient(json = get(), enableLogging = enableLogging) }

		singleOf(::MovieService)
		singleOf(::MovieClient)
	}
