/*
 * Cinelex
 * UITestCase
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.journey.support

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.core.os.LocaleListCompat
import androidx.test.core.app.ActivityScenario
import co.esekiels.cinelex.MainActivity
import co.esekiels.cinelex.common.CinelexException
import co.esekiels.cinelex.common.ErrorConstants
import co.esekiels.cinelex.data.MovieRepository
import co.esekiels.cinelex.testing.FakeMovieRepository
import co.esekiels.cinelex.testing.MovieStubs
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp-xxhdpi")
abstract class UITestCase {
	@get:Rule
	val compose = createEmptyComposeRule()

	private var activity: ActivityScenario<MainActivity>? = null

	fun launchApp(scenario: Scenario) {
		loadKoinModules(module { single<MovieRepository> { scenario.repository() } })
		activity = ActivityScenario.launch(MainActivity::class.java)
	}

	@After
	fun tearDown() {
		activity?.close()
		AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
		// Robolectric builds a fresh CinelexApplication per test, which calls startKoin again.
		stopKoin()
	}

	enum class Scenario {
		Loaded,
		Loading,
		Empty,
		Error,
		;

		fun repository(): MovieRepository =
			when (this) {
				Loaded -> FakeMovieRepository(MovieStubs.all)
				Loading -> FakeMovieRepository(emptyList(), isLoading = true)
				Empty -> FakeMovieRepository(emptyList())
				Error ->
					FakeMovieRepository(emptyList()).apply {
						failure = CinelexException(ErrorConstants.NETWORK_ERROR, "Server unreachable")
					}
			}
	}

	companion object {
		const val TIMEOUT_MS = 5_000L
	}
}
