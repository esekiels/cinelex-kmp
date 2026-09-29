//
//  UITestScenario.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

#if DEBUG
import Foundation
import Shared

enum UITestScenario: String {

    case loaded
    case loading
    case empty
    case error
    case detailError

    /// `nil` unless the app was launched with `-UITestStubs`.
    static var current: Self? {
        let arguments = ProcessInfo.processInfo.arguments
        guard arguments.contains("-UITestStubs") else {
            return nil
        }
        guard
            let flag = arguments.firstIndex(of: "-UITestScenario"),
            let name = arguments[safe: flag + 1],
            let scenario = Self(rawValue: name)
        else {
            return .loaded
        }
        return scenario
    }

    var repository: any MovieRepository {
        switch self {
        case .loaded:
            return FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
        case .loading:
            return FakeMovieRepository(nowPlaying: [], isLoading: true)
        case .empty:
            return FakeMovieRepository(nowPlaying: [])
        case .error:
            let repository = FakeMovieRepository(nowPlaying: [])
            repository.failure = CinelexException(
                code: ErrorConstants.shared.NETWORK_ERROR,
                message: "Server unreachable"
            )
            return repository
        case .detailError:
            let repository = FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
            repository.detailsFailure = CinelexException(
                code: ErrorConstants.shared.NETWORK_ERROR,
                message: "Server unreachable"
            )
            return repository
        }
    }
}

private extension Array {

    subscript(safe index: Int) -> Element? {
        indices.contains(index) ? self[index] : nil
    }
}
#endif
