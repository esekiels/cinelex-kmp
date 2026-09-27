package co.esekiels.cinelex

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import co.esekiels.cinelex.core.design.CinelexTheme
import co.esekiels.cinelex.feature.home.HomeScreen
import co.esekiels.cinelex.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
	private val homeViewModel: HomeViewModel by viewModel()

	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		setContent {
			CinelexTheme {
				HomeScreen(homeViewModel)
			}
		}
	}
}
