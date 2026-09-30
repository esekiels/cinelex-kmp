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

		detail.waitForText("Tim Robbins")
		detail.text("Cast").assertExists()
		detail.text("Director").assertExists()
		detail.text("Frank Darabont").assertExists()
		detail.text("Official Trailer").assertExists()
	}

	@Test
	fun detail_backReturnsHome() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		home.openMovie("The Shawshank Redemption")
		detail.waitForText("Tim Robbins")

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
		detail.text("Couldn't load movie").assertIsDisplayed()
		detail.text("Please check your internet connection and try again.").assertIsDisplayed()
		detail.text("Retry").assertIsDisplayed()
	}
}
