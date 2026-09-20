//
//  JourneyTests.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import XCTest

@MainActor
final class JourneyTests: UITestCase {

    private var home: HomeScreen { HomeScreen(app: app) }

    // MARK: - Loaded

    func testLaunch_resolvesFromSkeletonToCarousels() {
        launchApp(.loaded)

        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))
        XCTAssertTrue(
            home.skeleton.waitForNonExistence(timeout: Self.timeout),
            "Skeleton should hand off to the carousels"
        )
        XCTAssertTrue(home.header("Now Playing").exists)
        XCTAssertTrue(home.header("Popular").exists)
    }

    func testScroll_reachesLowerSections() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))

        XCTAssertTrue(home.scrollToSection("Top Rated"), "Top Rated should scroll into reach")
        XCTAssertTrue(home.scrollToSection("Upcoming"), "Upcoming should scroll into reach")
    }

    func testCarousel_scrollsToSecondMovie() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))

        XCTAssertTrue(
            home.scrollCarousel("Popular", toCard: "The Godfather"),
            "The second stub should be reachable by swiping the carousel"
        )
    }

    // MARK: - Other states

    func testLoading_showsSkeleton() {
        launchApp(.loading)

        XCTAssertTrue(home.skeleton.waitForExistence(timeout: Self.timeout))
        XCTAssertFalse(home.firstCard.exists, "Skeleton and cards should never coexist")
    }

    func testEmpty_showsUnavailableView() {
        launchApp(.empty)

        XCTAssertTrue(home.emptyState.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(app.staticTexts["No movies"].exists)
    }

    func testError_showsMessage() {
        launchApp(.error)

        XCTAssertTrue(home.errorState.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(app.staticTexts["Something went wrong"].exists)
        XCTAssertTrue(
            app.staticTexts["Server unreachable"].exists,
            "The CinelexException message should reach the description"
        )
    }
}
