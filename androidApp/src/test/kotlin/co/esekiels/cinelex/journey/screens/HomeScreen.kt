/*
 * Cinelex
 * HomeScreen
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.journey.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.support.UITestCase.Companion.TIMEOUT_MS

class HomeScreen(
	private val compose: ComposeTestRule,
) {
	val cards: SemanticsNodeInteractionCollection get() = compose.onAllNodesWithTag(TestTag.MOVIE_CARD)

	val skeleton: SemanticsNodeInteraction get() = compose.onAllNodesWithTag(TestTag.HOME_SKELETON).onFirst()

	val emptyState: SemanticsNodeInteraction get() = compose.onNodeWithTag(TestTag.HOME_EMPTY)

	val errorState: SemanticsNodeInteraction get() = compose.onNodeWithTag(TestTag.HOME_ERROR)

	fun text(text: String): SemanticsNodeInteraction = compose.onNodeWithText(text)

	private fun card(
		movieTitle: String,
		carouselTitle: String,
	): SemanticsNodeInteraction =
		compose.onNode(hasContentDescription(movieTitle) and hasAnyAncestor(hasTestTag(TestTag.carousel(carouselTitle))))

	fun selectLanguage(name: String) {
		compose.onNodeWithContentDescription("Change language").performClick()
		text(name).performClick()
	}

	val themeMenu: SemanticsNodeInteraction get() = compose.onNodeWithContentDescription("Change theme")

	fun selectTheme(name: String) {
		themeMenu.performClick()
		text(name).performClick()
	}

	fun waitForText(
		text: String,
		timeout: Long = TIMEOUT_MS,
	) {
		compose.waitUntil(timeout) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
	}

	fun waitUntilLoaded(timeout: Long = TIMEOUT_MS) {
		compose.waitUntil(timeout) { cards.fetchSemanticsNodes().isNotEmpty() }
	}

	fun scrollToSection(title: String): SemanticsNodeInteraction {
		compose.onNodeWithTag(TestTag.HOME_LIST).performScrollToNode(hasText(title))
		return text(title)
	}

	fun scrollCarousel(
		title: String,
		toCard: String,
	): SemanticsNodeInteraction {
		scrollToSection(title)
		compose.onNodeWithTag(TestTag.carousel(title)).performScrollToNode(hasContentDescription(toCard))
		return card(toCard, title)
	}
}
