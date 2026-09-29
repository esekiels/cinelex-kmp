//
//  CinelexDIFactory.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Shared

@MainActor
final class CinelexDIFactory {

    private let movieRepository: any MovieRepository
    private let userDataRepository: any UserDataRepository

    init(
        movieRepository: any MovieRepository = KoinHelperKt.movieRepository(),
        userDataRepository: any UserDataRepository = KoinHelperKt.userDataRepository()
    ) {
        self.movieRepository = movieRepository
        self.userDataRepository = userDataRepository
    }

    func injectHomeViewModel() -> HomeViewModel {
        HomeViewModel(repository: movieRepository)
    }

    func injectDetailViewModel(movieId: Int32) -> DetailViewModel {
        DetailViewModel(repository: movieRepository, movieId: movieId)
    }

    func injectPreferencesStore() -> PreferencesStore {
        PreferencesStore(repository: userDataRepository)
    }
}
