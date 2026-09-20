/*
 * Cinelex
 * CinelexApplication
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex

import android.app.Application
import co.esekiels.cinelex.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CinelexApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		initKoin(enableLogging = BuildConfig.DEBUG) {
			androidLogger()
			androidContext(this@CinelexApplication)
		}
	}
}
