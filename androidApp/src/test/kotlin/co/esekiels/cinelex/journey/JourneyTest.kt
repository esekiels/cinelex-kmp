/*
 * Cinelex
 * JourneyTest
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.journey

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.core.os.LocaleListCompat
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.screens.HomeScreen
import co.esekiels.cinelex.journey.support.UITestCase
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

	// Detail

	@Test
	fun detail_showsSectionsFromTheSharedModel() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.openMovie("The Shawshank Redemption")

		home.waitForText("Tim Robbins")
		home.text("Cast").assertExists()
		home.text("Director").assertExists()
		home.text("Frank Darabont").assertExists()
		home.text("Official Trailer").assertExists()
	}

	@Test
	fun detail_backReturnsHome() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		home.openMovie("The Shawshank Redemption")
		home.waitForText("Tim Robbins")

		compose.onNodeWithContentDescription("Back").performClick()

		home.waitUntilLoaded()
		home.text("Now Playing").assertIsDisplayed()
	}

	@Test
	fun detail_error_showsMessage() {
		launchApp(Scenario.DetailError)
		home.waitUntilLoaded()

		home.openMovie("The Shawshank Redemption")

		compose.waitUntil(TIMEOUT_MS) { compose.onAllNodesWithTag(TestTag.DETAIL_ERROR).fetchSemanticsNodes().isNotEmpty() }
		home.text("Please check your internet connection and try again.").assertIsDisplayed()
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
	fun language_savesSelection() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.selectLanguage("Indonesian")

		home.waitForText("Sedang Tayang")
		assertEquals(Language.INDONESIAN, preferences.userPreferences.value.language)
	}

	@Config(sdk = [34])
	@Test
	fun language_adoptsSystemSettingOnAndroid13() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("id"))
		recreateApp()
		home.waitUntilLoaded()

		assertEquals(Language.INDONESIAN, preferences.userPreferences.value.language)
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

	// Theme

	@Test
	fun theme_switchesToDark() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		assertFalse(isNightMode())

		home.selectTheme("Dark")

		home.waitUntilLoaded()
		assertTrue(isNightMode())
	}

	@Test
	fun theme_savesSelection() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		home.selectTheme("Dark")

		assertEquals(UiTheme.DARK, preferences.userPreferences.value.uiTheme)
	}

	@Test
	fun theme_updatesMenuWhenAppearanceUnchanged() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		home.themeMenu.assert(hasStateDescription("System"))

		home.selectTheme("Light")

		home.themeMenu.assert(hasStateDescription("Light"))
		assertFalse(isNightMode())
	}
}
