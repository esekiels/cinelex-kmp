//
//  DetailViewModel.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Foundation
import KMPNativeCoroutinesAsync
import Shared

@Observable
@MainActor
final class DetailViewModel {

    private(set) var state: UiState<MovieDetails> = .loading

    private let repository: any MovieRepository
    private let movieId: Int32

    init(repository: any MovieRepository, movieId: Int32) {
        self.repository = repository
        self.movieId = movieId
    }

    func load() async {
        if case .loaded = state {
            return
        }
        state = .loading
        do {
            let details = try await asyncFunction(for: repository.fetchMovieDetails(id: movieId))
            state = .loaded(details)
        } catch is CancellationError {
            return
        } catch {
            let exception = error.cinelexException
            state = .error(
                code: exception?.code ?? ErrorConstants.shared.UNKNOWN_ERROR,
                message: exception?.message ?? error.localizedDescription
            )
        }
    }
}
