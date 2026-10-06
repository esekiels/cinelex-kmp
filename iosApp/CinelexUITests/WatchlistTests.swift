//
//  WatchlistTests.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 06/10/26.
//

import XCTest

@MainActor
final class WatchlistTests: UITestCase {

    func testWatchlist_emptyShowsHint() {
        launchApp(.loaded)

        watchlist.open()

        XCTAssertTrue(watchlist.emptyState.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(app.staticTexts["Your watchlist is empty"].exists)
    }

    func testWatchlist_savedFromDetailIsListedAndRemovable() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))
        home.firstCard.tap()
        XCTAssertTrue(detail.watchlistToggle.waitForExistence(timeout: Self.timeout))

        detail.watchlistToggle.tap()

        XCTAssertTrue(detail.watchlistToggle.wait(for: \.label, toEqual: "Remove from watchlist", timeout: Self.timeout))
        detail.backButton.tap()
        watchlist.open()
        let row = watchlist.row("The Shawshank Redemption")
        XCTAssertTrue(row.waitForExistence(timeout: Self.timeout))
        XCTAssertEqual(watchlist.rows.count, 1)

        row.tap()
        XCTAssertTrue(detail.watchlistToggle.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(detail.watchlistToggle.wait(for: \.label, toEqual: "Remove from watchlist", timeout: Self.timeout))
        detail.watchlistToggle.tap()
        XCTAssertTrue(detail.watchlistToggle.wait(for: \.label, toEqual: "Add to watchlist", timeout: Self.timeout))
        detail.backButton.tap()

        XCTAssertTrue(watchlist.emptyState.waitForExistence(timeout: Self.timeout))
    }
}
