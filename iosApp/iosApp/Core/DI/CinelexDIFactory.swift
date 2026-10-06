//
//  CinelexDIFactory.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import SwiftUI
import Shared

@MainActor
final class CinelexDIFactory {

    private let movieRepository: any MovieRepository
    private let userDataRepository: any UserDataRepository
    private let watchlistRepository: any WatchlistRepository

    init(
        movieRepository: any MovieRepository = KoinHelperKt.movieRepository(),
        userDataRepository: any UserDataRepository = KoinHelperKt.userDataRepository(),
        watchlistRepository: any WatchlistRepository = KoinHelperKt.watchlistRepository()
    ) {
        self.movieRepository = movieRepository
        self.userDataRepository = userDataRepository
        self.watchlistRepository = watchlistRepository
    }

    func injectHomeViewModel() -> HomeViewModel {
        HomeViewModel(repository: movieRepository)
    }

    func injectDetailViewModel(movieId: Int32) -> DetailViewModel {
        DetailViewModel(repository: movieRepository, watchlist: watchlistRepository, movieId: movieId)
    }

    func injectSearchViewModel() -> SearchViewModel {
        SearchViewModel(repository: movieRepository)
    }

    func injectWatchlistViewModel() -> WatchlistViewModel {
        WatchlistViewModel(repository: watchlistRepository)
    }

    func injectPreferencesStore() -> PreferencesStore {
        PreferencesStore(repository: userDataRepository)
    }
}

extension EnvironmentValues {
    // Optional: a non-nil default would resolve repositories from Koin, which may not be started.
    @Entry var factory: CinelexDIFactory?
}
