//
//  SearchScreen.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import XCTest

@MainActor
struct SearchScreen {

    let app: XCUIApplication

    var tab: XCUIElement { app.tabBars.buttons["Search"].firstMatch }

    var field: XCUIElement { app.searchFields.firstMatch }

    var firstRecommendation: XCUIElement { rows(AccessibilityID.recommendationRow).element(boundBy: 0) }

    var firstResult: XCUIElement { rows(AccessibilityID.searchResultRow).element(boundBy: 0) }

    var emptyState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.searchEmpty].firstMatch
    }

    var errorState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.searchError].firstMatch
    }

    func result(_ title: String) -> XCUIElement {
        rows(AccessibilityID.searchResultRow).matching(NSPredicate(format: "label == %@", title)).firstMatch
    }

    func open() {
        tab.tap()
    }

    func search(_ query: String) {
        field.tap()
        field.typeText(query)
    }

    private func rows(_ identifier: String) -> XCUIElementQuery {
        app.descendants(matching: .any).matching(identifier: identifier)
    }
}
