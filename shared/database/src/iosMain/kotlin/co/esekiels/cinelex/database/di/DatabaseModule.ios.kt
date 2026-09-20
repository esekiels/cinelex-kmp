/*
 * Cinelex
 * DatabaseModule
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex.database.di

import androidx.room.RoomDatabase
import co.esekiels.cinelex.database.CinelexDatabase
import co.esekiels.cinelex.database.databaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databasePlatformModule: Module =
	module {
		single<RoomDatabase.Builder<CinelexDatabase>> { databaseBuilder() }
	}
