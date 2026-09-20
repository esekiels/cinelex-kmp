/*
 * Cinelex
 * Koin
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.data.di

import co.esekiels.cinelex.common.di.commonModule
import co.esekiels.cinelex.database.di.databaseModule
import co.esekiels.cinelex.network.di.networkModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Android passes `androidContext(...)`
 * Swift cannot see Kotlin default argument
 * iOS entry point is separate zero-argument helper in `:shared:umbrella`
 */
fun initKoin(
	enableLogging: Boolean = false,
	config: KoinAppDeclaration? = null,
): KoinApplication =
	startKoin {
		config?.invoke(this)
		modules(
			commonModule,
			networkModule(enableLogging),
			databaseModule,
			dataModule,
		)
	}
