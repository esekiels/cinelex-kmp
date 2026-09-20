//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesAsync
import Shared

@Observable
@MainActor
final class HomeViewModel {
    
    private(set) var state = HomeState()
    
    private let repository: any MovieRepository
    private var tasks: [Task<Void, Never>] = []
    private var carousels = HomeState.Carousels()
    
    init(repository: any MovieRepository) {
        self.repository = repository
    }
    
    func onAppear() {
        guard tasks.isEmpty else {
            return
        }
        tasks = [
            observe(repository.observeNowPlaying(), into: \.nowPlaying),
            observe(repository.observePopular(), into: \.popular),
            observe(repository.observeUpcoming(), into: \.upcoming),
            observe(repository.observeTopRated(), into: \.topRated),
            Task { await self.refresh() }
        ]
    }
    
    func onDisappear() {
        tasks.forEach { $0.cancel() }
        tasks.removeAll()
    }
    
    func refresh() async {
        do {
            try await asyncFunction(for: repository.refreshMovies())
            if carousels.isEmpty {
                state.uiState = .empty
            }
        } catch is CancellationError {
            return
        } catch {
            fail(error)
        }
    }
}

private extension HomeViewModel {
    
    func observe<Failure: Error, Unit>(
        _ flow: @escaping NativeFlow<[Movie], Failure, Unit>,
        into keyPath: WritableKeyPath<HomeState.Carousels, [Movie]>
    ) -> Task<Void, Never> {
        Task {
            do {
                for try await movies in asyncSequence(for: flow) {
                    self.carousels[keyPath: keyPath] = movies
                    if !self.carousels.isEmpty {
                        self.state.uiState = .loaded(self.carousels)
                    }
                }
            } catch is CancellationError {
                return
            } catch {
                self.fail(error)
            }
        }
    }
    
    func fail(_ error: Error) {
        if let exception = error.cinelexException, carousels.isEmpty {
            state.uiState = .error(
                code: exception.code,
                message: exception.message
            )
        }
    }
}
