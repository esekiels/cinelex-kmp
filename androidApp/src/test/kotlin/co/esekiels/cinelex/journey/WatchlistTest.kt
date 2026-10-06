/*
 * Cinelex
 * WatchlistTest
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.journey

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import co.esekiels.cinelex.journey.support.UITestCase
import org.junit.Assert.assertEquals
import org.junit.Test

class WatchlistTest : UITestCase() {
	@Test
	fun watchlist_emptyShowsHint() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		watchlist.open()

		watchlist.waitForEmpty()
		watchlist.text("Your watchlist is empty").assertIsDisplayed()
	}

	@Test
	fun watchlist_savedFromDetailIsListedAndRemovable() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		home.openMovie("The Shawshank Redemption")
		detail.waitForText("Tim Robbins")

		detail.watchlistToggle.performClick()

		detail.watchlistToggle.assertContentDescriptionEquals("Remove from watchlist")
		detail.back()
		watchlist.open()
		watchlist.waitForRow("The Shawshank Redemption")
		assertEquals(1, watchlist.rowCount())

		watchlist.row("The Shawshank Redemption").performClick()
		detail.waitForText("Tim Robbins")
		detail.watchlistToggle.performClick()
		detail.back()

		watchlist.waitForEmpty()
	}
}
