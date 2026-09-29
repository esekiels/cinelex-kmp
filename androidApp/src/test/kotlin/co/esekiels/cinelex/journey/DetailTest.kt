/*
 * Cinelex
 * DetailTest
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.journey

import androidx.compose.ui.test.assertIsDisplayed
import co.esekiels.cinelex.journey.support.UITestCase
import org.junit.Test

class DetailTest : UITestCase() {
	// Loaded

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

		detail.back()

		home.waitUntilLoaded()
		home.text("Now Playing").assertIsDisplayed()
	}

	// Error

	@Test
	fun detail_error_showsMessage() {
		launchApp(Scenario.DetailError)
		home.waitUntilLoaded()

		home.openMovie("The Shawshank Redemption")

		detail.waitForError()
		home.text("Please check your internet connection and try again.").assertIsDisplayed()
	}
}
