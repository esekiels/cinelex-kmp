/*
 * Cinelex
 * JourneyTest
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.journey

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.screens.HomeScreen
import co.esekiels.cinelex.journey.support.UITestCase
import org.junit.Test
import org.robolectric.annotation.Config

class JourneyTest : UITestCase() {
	private val home = HomeScreen(compose)

	// Loaded

	@Test
	fun launch_resolvesFromSkeletonToCarousels() {
		launchApp(Scenario.Loaded)

		home.waitUntilLoaded()
		compose.onAllNodesWithTag(TestTag.HOME_SKELETON).assertCountEquals(0)
		home.text("Now Playing").assertIsDisplayed()
		home.text("Popular").assertIsDisplayed()
	}

	@Test
	fun scroll_reachesLowerSections() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.scrollToSection("Top Rated").assertIsDisplayed()
		home.scrollToSection("Upcoming").assertIsDisplayed()
	}

	@Test
	fun carousel_scrollsToSecondMovie() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.scrollCarousel("Popular", toCard = "The Godfather").assertIsDisplayed()
	}

	// Other states

	@Test
	fun loading_showsSkeleton() {
		launchApp(Scenario.Loading)

		home.skeleton.assertIsDisplayed()
		home.cards.assertCountEquals(0)
	}

	@Test
	fun empty_showsUnavailableView() {
		launchApp(Scenario.Empty)

		home.emptyState.assertIsDisplayed()
		home.text("No movies").assertIsDisplayed()
	}

	@Test
	fun error_showsMessage() {
		launchApp(Scenario.Error)

		home.errorState.assertIsDisplayed()
		home.text("Something went wrong").assertIsDisplayed()
		home.text("Please check your internet connection and try again.").assertIsDisplayed()
	}

	// Language

	@Config(sdk = [32])
	@Test
	fun language_switchesToIndonesian() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.selectLanguage("Indonesian")

		home.waitForText("Sedang Tayang")
		home.text("Populer").assertIsDisplayed()
	}

	@Config(sdk = [32])
	@Test
	fun language_localizesErrorMessage() {
		launchApp(Scenario.Error)
		home.errorState.assertIsDisplayed()

		home.selectLanguage("Indonesian")

		home.waitForText("Terjadi kesalahan")
		home.text("Periksa koneksi internet Anda dan coba lagi.").assertIsDisplayed()
	}
}
