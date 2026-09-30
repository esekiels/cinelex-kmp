//
//  DetailTests.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import XCTest

@MainActor
final class DetailTests: UITestCase {

    // MARK: - Loaded

    func testDetail_opensFromCardAndGoesBack() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))

        home.firstCard.tap()

        XCTAssertTrue(detail.root.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(detail.text("The Shawshank Redemption").exists)
        XCTAssertTrue(detail.text("Drama, Crime · 1994 · 2h 22m").exists)

        detail.backButton.tap()

        XCTAssertTrue(home.firstCard.waitForExistence(timeout: Self.timeout), "Back should return to the carousels")
        XCTAssertFalse(detail.root.exists)
    }

    func testDetail_showsCastCrewAndTrailer() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))
        home.firstCard.tap()
        XCTAssertTrue(detail.root.waitForExistence(timeout: Self.timeout))

        XCTAssertTrue(detail.scrollTo(detail.firstCastCard), "Cast should be reachable")
        XCTAssertEqual(detail.firstCastCard.label, "Tim Robbins, Andy Dufresne")
        XCTAssertTrue(detail.scrollTo(detail.text("Frank Darabont")), "Director should be listed under crew")
        XCTAssertTrue(detail.scrollTo(detail.trailerButton), "Trailer should be reachable")
    }

    func testDetail_localizesSections() {
        launchApp(.loaded)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))
        home.selectLanguage("Indonesian")

        home.firstCard.tap()

        XCTAssertTrue(detail.root.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(detail.scrollTo(detail.text("Pemeran")), "Cast header should be in Indonesian")
    }

    // MARK: - Error

    func testDetail_errorShowsMessage() {
        launchApp(.detailError)
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.timeout))

        home.firstCard.tap()

        XCTAssertTrue(detail.errorState.waitForExistence(timeout: Self.timeout))
        XCTAssertTrue(detail.text("Couldn't load movie").exists)
        XCTAssertTrue(detail.text("Please check your internet connection and try again.").exists)
        XCTAssertTrue(app.buttons["Retry"].exists, "Error state should offer a retry")
    }
}
