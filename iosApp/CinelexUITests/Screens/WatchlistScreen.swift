//
//  WatchlistScreen.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 06/10/26.
//

import XCTest

@MainActor
struct WatchlistScreen {

    let app: XCUIApplication

    var tab: XCUIElement { app.tabBars.buttons["Watchlist"].firstMatch }

    var emptyState: XCUIElement {
        app.descendants(matching: .any)[AccessibilityID.watchlistEmpty].firstMatch
    }

    var rows: XCUIElementQuery {
        app.descendants(matching: .any).matching(identifier: AccessibilityID.watchlistRow)
    }

    func row(_ title: String) -> XCUIElement {
        rows.matching(NSPredicate(format: "label == %@", title)).firstMatch
    }

    func open() {
        tab.tap()
    }
}
