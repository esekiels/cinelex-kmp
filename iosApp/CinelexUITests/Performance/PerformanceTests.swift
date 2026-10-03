//
//  PerformanceTests.swift
//  CinelexUITests
//
//  Created by Esekiel Surbakti on 03/10/26.
//

import XCTest

@MainActor
final class PerformanceTests: UITestCase {

    nonisolated static let liveTimeout: TimeInterval = 15

    func testHomeScroll() {
        launchHome()
        measure(metrics: metrics(XCTOSSignpostMetric.scrollDecelerationMetric), options: Self.manuallyStopped) {
            app.swipeUp(velocity: .fast)
            stopMeasuring()
            // Slow so the reset can't overshoot the top into pull-to-refresh.
            app.swipeDown(velocity: .slow)
        }
    }

    func testOpenDetail() {
        launchHome()
        // Warm-up: the first open fetches from the network, later ones hit the cache.
        openDetail()
        closeDetail()
        measure(metrics: metrics(XCTOSSignpostMetric.navigationTransitionMetric), options: Self.manuallyStopped) {
            openDetail()
            stopMeasuring()
            closeDetail()
        }
    }

    func testDetailScroll() {
        launchHome()
        openDetail()
        measure(metrics: metrics(XCTOSSignpostMetric.scrollDecelerationMetric), options: Self.manuallyStopped) {
            detail.root.swipeUp(velocity: .fast)
            stopMeasuring()
            detail.root.swipeDown(velocity: .fast)
        }
    }
}

private extension PerformanceTests {

    static var manuallyStopped: XCTMeasureOptions {
        let options = XCTMeasureOptions()
        options.invocationOptions = .manuallyStop
        options.iterationCount = 5
        return options
    }

    func launchHome() {
        launchLive()
        XCTAssertTrue(home.waitUntilLoaded(timeout: Self.liveTimeout), "Home should load from the network")
    }

    func openDetail() {
        home.firstCard.tap()
        XCTAssertTrue(detail.root.waitForExistence(timeout: Self.liveTimeout), "Detail should load")
    }

    func closeDetail() {
        detail.backButton.tap()
        XCTAssertTrue(detail.root.waitForNonExistence(timeout: Self.timeout), "Back should pop Detail")
    }

    /// `XCTHitchMetric` is iOS 26+; older devices fall back to the signpost metric's own hitch ratio.
    func metrics(_ signpost: any XCTMetric) -> [any XCTMetric] {
        if #available(iOS 26, *) {
            return [XCTHitchMetric(application: app), signpost]
        }
        return [signpost]
    }
}
