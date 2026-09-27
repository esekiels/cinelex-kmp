//
//  AccessibilityID.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

enum AccessibilityID {

    static let movieCard = "MovieCard"
    static let homeSkeleton = "HomeSkeleton"
    static let homeEmpty = "HomeEmpty"
    static let homeError = "HomeError"
    static let localeMenu = "LocaleMenu"
    static let themeMenu = "ThemeMenu"

    static func carousel(_ title: String) -> String { "Carousel-\(title)" }
}
