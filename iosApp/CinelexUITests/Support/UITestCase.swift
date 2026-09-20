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

    override func setUp() {
        continueAfterFailure = false
    }

    func launchApp(_ scenario: Scenario = .loaded) {
        app.launchArguments = ["-UITestStubs", "-UITestScenario", scenario.rawValue]
        app.launch()
    }

    enum Scenario: String {
        case loaded
        case loading
        case empty
        case error
    }
}

// swiftlint:enable test_case_accessibility
