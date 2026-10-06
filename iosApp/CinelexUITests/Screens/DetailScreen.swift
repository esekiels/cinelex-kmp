//
//  DetailScreen.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import XCTest

@MainActor
struct DetailScreen {

    let app: XCUIApplication

    var root: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.detailScreen].firstMatch
    }

    var errorState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.detailError].firstMatch
    }

    var firstCastCard: XCUIElement {
        app.descendants(matching: .any).matching(identifier: AccessibilityID.castCard).element(boundBy: 0)
    }

    var trailerButton: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.trailerButton].firstMatch
    }

    var watchlistToggle: XCUIElement {
        app.buttons[AccessibilityID.watchlistToggle].firstMatch
    }

    var backButton: XCUIElement {
        app.navigationBars.buttons.element(boundBy: 0)
    }

    func text(_ value: String) -> XCUIElement {
        app.staticTexts[value].firstMatch
    }

    @discardableResult
    func scrollTo(_ element: XCUIElement, attempts limit: Int = 6) -> Bool {
        var attempts = 0
        while !element.isHittable, attempts < limit {
            root.swipeUp()
            attempts += 1
        }
        return element.isHittable
    }
}
