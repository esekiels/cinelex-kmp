package co.esekiels.cinelex

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.esekiels.cinelex.core.common.applyLanguage
import co.esekiels.cinelex.core.common.applyUiTheme
import co.esekiels.cinelex.core.common.systemAppLanguage
import co.esekiels.cinelex.core.design.CinelexTheme
import co.esekiels.cinelex.core.design.LanguageMenu
import co.esekiels.cinelex.core.design.ThemeMenu
import co.esekiels.cinelex.feature.home.HomeScreen
import co.esekiels.cinelex.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
	private val mainViewModel: MainViewModel by viewModel()
	private val homeViewModel: HomeViewModel by viewModel()

	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			mainViewModel.syncLanguage(systemAppLanguage())
		}

		setContent {
			val preferences by mainViewModel.preferences.collectAsStateWithLifecycle()
			CinelexTheme {
				HomeScreen(homeViewModel) {
					LanguageMenu(preferences.language) {
						mainViewModel.setLanguage(it)
						applyLanguage(it)
					}
					ThemeMenu(preferences.uiTheme) {
						mainViewModel.setUiTheme(it)
						applyUiTheme(it)
					}
				}
			}
		}
	}
}
