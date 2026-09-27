/*
 * Cinelex
 * CinelexApplication
 *
 * Created by Esekiel Surbakti on 20/09/26
 */

package co.esekiels.cinelex

import android.app.Application
import android.os.Build
import co.esekiels.cinelex.core.common.applyLanguage
import co.esekiels.cinelex.core.common.applyUiTheme
import co.esekiels.cinelex.core.di.appModule
import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.data.di.initKoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CinelexApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		initKoin(enableLogging = BuildConfig.DEBUG) {
			androidLogger()
			androidContext(this@CinelexApplication)
			modules(appModule)
		}
		val preferences = runBlocking { get<UserDataRepository>().userPreferences.first() }
		applyUiTheme(preferences.uiTheme)
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) applyLanguage(preferences.language)
	}
}
