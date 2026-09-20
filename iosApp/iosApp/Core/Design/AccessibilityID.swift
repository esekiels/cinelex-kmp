//
//  AccessibilityID.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

/// Identifiers the views publish and `CinelexUITests` queries.
///
/// One definition, compiled into both targets — the test bundle links no app
/// module, so it gets this file by source membership rather than by import.
/// A rename therefore breaks the build instead of silently failing a test.
enum AccessibilityID {

    static let movieCard = "MovieCard"
    static let homeSkeleton = "HomeSkeleton"
    static let homeEmpty = "HomeEmpty"
    static let homeError = "HomeError"

    /// Every carousel is fed the same movies, so a card lookup has to be
    /// scoped to one section or it matches in all four.
    static func carousel(_ title: String) -> String { "Carousel-\(title)" }
}
