/*
 * Cinelex
 * CinelexApplication
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex

import android.app.Application
import co.esekiels.cinelex.core.design.applySavedTheme
import co.esekiels.cinelex.core.di.appModule
import co.esekiels.cinelex.data.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CinelexApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		applySavedTheme()
		initKoin(enableLogging = BuildConfig.DEBUG) {
			androidLogger()
			androidContext(this@CinelexApplication)
			modules(appModule)
		}
	}
}
