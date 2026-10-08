/*
 * Cinelex
 * SearchScreen
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.journey.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS

class SearchScreen(
	compose: ComposeTestRule,
) : Page(compose) {
	fun open() {
		compose.onNodeWithText("Search").performClick()
	}

	fun search(query: String) {
		compose.onNode(hasSetTextAction()).performTextInput(query)
	}

	fun result(title: String): SemanticsNodeInteraction =
		compose.onNode(hasTestTag(TestTag.SEARCH_RESULT_ROW) and hasContentDescription(title))

	fun resultCount(): Int = compose.onAllNodesWithTag(TestTag.SEARCH_RESULT_ROW).fetchSemanticsNodes().size

	fun waitForResult(
		title: String,
		timeout: Long = TIMEOUT_MS,
	) {
		compose.waitUntil(timeout) {
			compose
				.onAllNodes(hasTestTag(TestTag.SEARCH_RESULT_ROW) and hasContentDescription(title))
				.fetchSemanticsNodes()
				.isNotEmpty()
		}
	}

	fun waitForTag(
		tag: String,
		timeout: Long = TIMEOUT_MS,
	) {
		compose.waitUntil(timeout) { compose.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty() }
	}
}
