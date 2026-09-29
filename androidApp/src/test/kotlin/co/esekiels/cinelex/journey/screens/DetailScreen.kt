/*
 * Cinelex
 * DetailScreen
 *
 * Created by Esekiel Surbakti on 29/09/26
 */

package co.esekiels.cinelex.journey.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS

class DetailScreen(
	private val compose: ComposeTestRule,
) {
	val errorState: SemanticsNodeInteraction get() = compose.onNodeWithTag(TestTag.DETAIL_ERROR)

	fun back() {
		compose.onNodeWithContentDescription("Back").performClick()
	}

	fun waitForError(timeout: Long = TIMEOUT_MS) {
		compose.waitUntil(timeout) { compose.onAllNodesWithTag(TestTag.DETAIL_ERROR).fetchSemanticsNodes().isNotEmpty() }
	}
}
