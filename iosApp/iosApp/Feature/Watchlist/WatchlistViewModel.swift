//
//  WatchlistViewModel.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 06/10/26.
//

import Foundation
import KMPNativeCoroutinesAsync
import Shared

@Observable
@MainActor
final class WatchlistViewModel {

    private(set) var state: UiState<[Movie]> = .loading

    private let repository: any WatchlistRepository

    init(repository: any WatchlistRepository) {
        self.repository = repository
    }

    func onAppear() async {
        do {
            for try await movies in asyncSequence(for: repository.observeWatchlist()) {
                state = movies.isEmpty ? .empty : .loaded(movies)
            }
        } catch is CancellationError {
            return
        } catch {
            state = .failed(error)
        }
    }
}
