//
//  HomeScreen.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import XCTest

@MainActor
struct HomeScreen {

    let app: XCUIApplication

    private var cards: XCUIElementQuery {
        app.descendants(matching: .any).matching(identifier: AccessibilityID.movieCard)
    }

    var firstCard: XCUIElement { cards.element(boundBy: 0) }

    var skeleton: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.homeSkeleton].firstMatch
    }

    var emptyState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.homeEmpty].firstMatch
    }

    var errorState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.homeError].firstMatch
    }

    var localeMenu: XCUIElement {
        app.buttons[AccessibilityID.localeMenu].firstMatch
    }

    func selectLanguage(_ name: String) {
        localeMenu.tap()
        app.buttons[name].firstMatch.tap()
    }

    func header(_ text: String) -> XCUIElement {
        app.staticTexts[text].firstMatch
    }

    func carousel(_ title: String) -> XCUIElement {
        app.scrollViews[AccessibilityID.carousel(title)].firstMatch
    }

    func card(_ movieTitle: String, in carousel: XCUIElement) -> XCUIElement {
        carousel.descendants(matching: .any)
            .matching(identifier: AccessibilityID.movieCard)
            .matching(NSPredicate(format: "label == %@", movieTitle))
            .firstMatch
    }

    func waitUntilLoaded(timeout: TimeInterval) -> Bool {
        firstCard.waitForExistence(timeout: timeout)
    }

    @discardableResult
    func scrollToSection(_ title: String, attempts limit: Int = 6) -> Bool {
        scroll(until: header(title), limit: limit) { app.swipeUp() }
    }

    @discardableResult
    func scrollCarousel(_ title: String, toCard movieTitle: String, attempts limit: Int = 4) -> Bool {
        let carousel = carousel(title)
        return scroll(until: card(movieTitle, in: carousel), limit: limit) { carousel.swipeLeft() }
    }

    private func scroll(
        until element: XCUIElement,
        limit: Int,
        swipe: () -> Void
    ) -> Bool {
        var attempts = 0
        while !element.isHittable, attempts < limit {
            swipe()
            attempts += 1
        }
        return element.isHittable
    }
}
