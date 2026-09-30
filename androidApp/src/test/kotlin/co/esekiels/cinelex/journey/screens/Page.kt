/*
 * Cinelex
 * Page
 *
 * Created by Esekiel Surbakti on 30/09/26
 */

package co.esekiels.cinelex.journey.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS

abstract class Page(
	protected val compose: ComposeTestRule,
) {
	fun text(text: String): SemanticsNodeInteraction = compose.onNodeWithText(text)

	fun waitForText(
		text: String,
		timeout: Long = TIMEOUT_MS,
	) {
		compose.waitUntil(timeout) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
	}
}
