/*
 * Cinelex
 * CommonModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val IO_DISPATCHER = named("io")

val commonModule: Module =
	module {
		single<CoroutineDispatcher>(IO_DISPATCHER) { Dispatchers.IO }
	}
