//
//  UITestCase.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import XCTest

// swiftlint:disable test_case_accessibility
@MainActor
class UITestCase: XCTestCase {

    let app = XCUIApplication()

    nonisolated static let timeout: TimeInterval = 5

    var home: HomeScreen { HomeScreen(app: app) }
    var detail: DetailScreen { DetailScreen(app: app) }
    var search: SearchScreen { SearchScreen(app: app) }
    var watchlist: WatchlistScreen { WatchlistScreen(app: app) }

    override func setUp() {
        continueAfterFailure = false
    }

    nonisolated static let localeArguments = ["-AppleLanguages", "(en)", "-AppleLocale", "en_US"]

    func launchApp(_ scenario: Scenario = .loaded) {
        app.launchArguments = ["-UITestStubs", "-UITestScenario", scenario.rawValue] + Self.localeArguments
        app.launch()
    }

    /// Launches against real data; stubs only exist in Debug builds anyway.
    func launchLive() {
        app.launchArguments = Self.localeArguments
        app.launch()
    }

    enum Scenario: String {
        case loaded
        case loading
        case empty
        case error
        case detailError
    }
}

// swiftlint:enable test_case_accessibility
