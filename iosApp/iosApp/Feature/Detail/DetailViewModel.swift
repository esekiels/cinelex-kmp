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
    private var refreshTask: Task<Void, Never>?

    init(repository: any MovieRepository, movieId: Int32) {
        self.repository = repository
        self.movieId = movieId
    }

    func onAppear() async {
        onRefresh()
        do {
            for try await details in asyncSequence(for: repository.observeMovieDetails(id: movieId)) {
                if let details {
                    state = .loaded(details)
                }
            }
        } catch is CancellationError {
            return
        } catch {
            fail(error)
        }
    }

    func onRefresh() {
        guard refreshTask == nil else {
            return
        }
        if case .error = state {
            state = .loading
        }
        refreshTask = Task {
            defer { refreshTask = nil }
            do {
                try await asyncFunction(for: repository.refreshMovieDetails(id: movieId))
            } catch is CancellationError {
                return
            } catch {
                fail(error)
            }
        }
    }
}

private extension DetailViewModel {

    func fail(_ error: Error) {
        if case .loaded = state {
            return
        }
        state = .failed(error)
    }
}
