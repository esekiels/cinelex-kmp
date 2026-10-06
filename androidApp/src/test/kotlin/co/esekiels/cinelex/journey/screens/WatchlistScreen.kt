/*
 * Cinelex
 * WatchlistScreen
 *
 * Created by Esekiel Surbakti on 06/10/26
 */

package co.esekiels.cinelex.journey.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS

class WatchlistScreen(
	compose: ComposeTestRule,
) : Page(compose) {
	fun open() {
		compose.onAllNodesWithText("Watchlist").onFirst().performClick()
	}

	fun row(title: String): SemanticsNodeInteraction = compose.onNode(isRow(title))

	fun rowCount(): Int = compose.onAllNodesWithTag(TestTag.WATCHLIST_ROW).fetchSemanticsNodes().size

	fun waitForEmpty(timeout: Long = TIMEOUT_MS) {
		compose.waitUntil(timeout) { compose.onAllNodesWithTag(TestTag.WATCHLIST_EMPTY).fetchSemanticsNodes().isNotEmpty() }
	}

	fun waitForRow(
		title: String,
		timeout: Long = TIMEOUT_MS,
	) {
		compose.waitUntil(timeout) {
			compose
				.onAllNodes(isRow(title))
				.fetchSemanticsNodes()
				.isNotEmpty()
		}
	}

	private fun isRow(title: String) = hasTestTag(TestTag.WATCHLIST_ROW) and hasContentDescription(title)
}
