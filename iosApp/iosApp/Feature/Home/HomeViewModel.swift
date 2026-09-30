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
    private var hasRefreshed = false
    
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
            Task { await self.refreshOnLanguageChange() }
        ]
        
        if !hasRefreshed {
            tasks.append(Task { await self.refresh() })
        }
    }
    
    func onDisappear() {
        tasks.forEach { $0.cancel() }
        tasks.removeAll()
    }
    
    func refresh() async {
        do {
            try await asyncFunction(for: repository.refreshMovies())
            hasRefreshed = true
            if carousels.isEmpty, try await !asyncFunction(for: repository.hasCachedMovies()).boolValue {
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
    
    func refreshOnLanguageChange() async {
        var isFirst = true
        do {
            for try await _ in asyncSequence(for: repository.observeContentLanguage()) {
                if !isFirst {
                    await refresh()
                }
                isFirst = false
            }
        } catch {
            return
        }
    }

    func fail(_ error: Error) {
        if carousels.isEmpty {
            state.uiState = .failed(error)
        }
    }
}
