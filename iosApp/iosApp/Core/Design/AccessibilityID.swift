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
    static let detailScreen = "DetailScreen"
    static let detailSkeleton = "DetailSkeleton"
    static let detailError = "DetailError"
    static let castCard = "CastCard"
    static let trailerButton = "TrailerButton"
    static let recommendationRow = "RecommendationRow"
    static let searchResultRow = "SearchResultRow"
    static let searchEmpty = "SearchEmpty"
    static let searchError = "SearchError"
    static let watchlistRow = "WatchlistRow"
    static let watchlistEmpty = "WatchlistEmpty"
    static let watchlistToggle = "WatchlistToggle"

    static func carousel(_ title: String) -> String { "Carousel-\(title)" }
}
