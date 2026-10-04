//
//  SearchTests.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import XCTest

@MainActor
final class SearchTests: UITestCase {

    // MARK: - Loaded

    func testSearch_showsRecommendationsBeforeTyping() {
        launchApp(.loaded)

        search.open()

        XCTAssertTrue(search.firstRecommendation.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(app.staticTexts["Recommendations"].exists)
        XCTAssertFalse(search.firstResult.exists, "Results should only appear after a query")
    }

    func testSearch_findsMovieAndOpensDetail() {
        launchApp(.loaded)
        search.open()

        search.search("god")

        let godfather = search.result("The Godfather")
        XCTAssertTrue(godfather.waitForExistence(timeout: Self.timeout), "Results should appear after the debounce")
        XCTAssertFalse(search.result("The Shawshank Redemption").exists, "Non-matching titles should be filtered out")

        godfather.tap()

        XCTAssertTrue(detail.root.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(detail.text("The Godfather").exists)
    }

    func testSearch_loadsNextPageAtTheEnd() {
        launchApp(.loaded)
        search.open()

        // The fake serves one match per page, so the second title only arrives via pagination.
        search.search("the")

        XCTAssertTrue(search.result("The Shawshank Redemption").waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(
            search.result("The Godfather").waitForExistence(timeout: Self.timeout),
            "Reaching the last row should load page 2"
        )
        XCTAssertTrue(search.result("The Shawshank Redemption").exists, "Page 2 should append, not replace")
    }

    func testSearch_noMatchShowsEmptyState() {
        launchApp(.loaded)
        search.open()

        search.search("zzz")

        XCTAssertTrue(search.emptyState.waitForExistence(timeout: Self.timeout))
        XCTAssertFalse(search.firstResult.exists)
    }

    // MARK: - Error

    func testSearch_errorShowsMessage() {
        launchApp(.error)
        search.open()

        search.search("god")

        XCTAssertTrue(search.errorState.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(app.staticTexts["Couldn't search movies"].exists)
        XCTAssertTrue(app.staticTexts["Please check your internet connection and try again."].exists)
    }
}
