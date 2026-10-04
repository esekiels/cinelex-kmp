/*
 * Cinelex
 * SearchTest
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.journey

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.journey.support.UITestCase
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchTest : UITestCase() {
	// Loaded

	@Test
	fun search_showsRecommendationsBeforeTyping() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()

		search.open()

		search.waitForTag(TestTag.RECOMMENDATION_ROW)
		search.text("Recommendations").assertIsDisplayed()
		assertEquals(0, search.resultCount())
	}

	@Test
	fun search_findsMovieAndOpensDetail() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		search.open()

		search.search("god")

		search.waitForResult("The Godfather")
		assertEquals("Non-matching titles should be filtered out", 1, search.resultCount())

		search.result("The Godfather").performClick()

		detail.waitForText("Tim Robbins")
		// Every stub shares Shawshank's cast, so the title is what proves the right movie opened.
		detail.waitForText("The Godfather")
	}

	@Test
	fun search_loadsNextPageAtTheEnd() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		search.open()

		// The fake serves one match per page, so the second title only arrives via pagination.
		search.search("the")

		search.waitForResult("The Godfather")
		search.result("The Shawshank Redemption").assertExists()
		assertEquals(2, search.resultCount())
	}

	@Test
	fun search_noMatchShowsEmptyState() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		search.open()

		search.search("zzz")

		search.waitForTag(TestTag.SEARCH_EMPTY)
		search.text("No results for “zzz”").assertIsDisplayed()
	}

	@Test
	fun search_backReturnsHome() {
		launchApp(Scenario.Loaded)
		home.waitUntilLoaded()
		search.open()
		search.waitForTag(TestTag.RECOMMENDATION_ROW)

		pressBack()

		home.waitUntilLoaded()
		home.text("Now Playing").assertIsDisplayed()
	}

	// Error

	@Test
	fun search_errorShowsMessage() {
		launchApp(Scenario.Error)
		search.open()

		search.search("god")

		search.waitForTag(TestTag.SEARCH_ERROR)
		search.text("Couldn't search movies").assertIsDisplayed()
		search.text("Please check your internet connection and try again.").assertIsDisplayed()
	}
}
