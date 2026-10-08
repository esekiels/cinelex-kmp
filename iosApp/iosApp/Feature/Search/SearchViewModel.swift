//
//  SearchViewModel.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import Foundation
import KMPNativeCoroutinesAsync
import Shared

@Observable
@MainActor
final class SearchViewModel {

    var query = ""

    private(set) var state = SearchState()

    var isQueryEmpty: Bool { trimmedQuery.isEmpty }

    private var trimmedQuery: String { query.trimmingCharacters(in: .whitespaces) }
    
    private var searchedQuery = ""
    private var page = 1
    private var totalPages = 1
    private var loadMoreTask: Task<Void, Never>?

    private let repository: any MovieRepository

    init(repository: any MovieRepository) {
        self.repository = repository
    }

    func onAppear() async {
        do {
            for try await recommendations in asyncSequence(for: repository.observeMovies(category: .popular))
            where !recommendations.isEmpty {
                state.recommendations = recommendations
            }
        } catch {
            return
        }
    }

    func search() async {
        let query = trimmedQuery
        guard query != searchedQuery else {
            return
        }
        loadMoreTask?.cancel()
        loadMoreTask = nil
        state.isLoadingMore = false
        guard !query.isEmpty else {
            searchedQuery = query
            state.uiState = nil
            return
        }
        do {
            try await Task.sleep(for: .milliseconds(500))
            state.uiState = .loading
            let result = try await asyncFunction(for: repository.searchMovies(query: query, page: 1))
            searchedQuery = query
            page = 1
            totalPages = Int(result.totalPages)
            state.uiState = result.movies.isEmpty ? .empty : .loaded(result.movies)
        } catch is CancellationError {
            return
        } catch {
            guard !Task.isCancelled else {
                return
            }
            searchedQuery = query
            state.uiState = .failed(error)
        }
    }

    func loadMoreIfNeeded(current movie: Movie) {
        guard
            case .loaded(let movies) = state.uiState,
            movie.id == movies.last?.id,
            page < totalPages,
            !state.isLoadingMore
        else {
            return
        }
        let query = searchedQuery
        state.isLoadingMore = true
        loadMoreTask = Task {
            defer {
                if !Task.isCancelled {
                    state.isLoadingMore = false
                }
            }
            do {
                while page < totalPages {
                    let next = page + 1
                    let result = try await asyncFunction(for: repository.searchMovies(query: query, page: Int32(next)))
                    guard !Task.isCancelled, case .loaded(let current) = state.uiState else {
                        return
                    }
                    page = next
                    totalPages = Int(result.totalPages)
                    let seen = Set(current.map(\.id))
                    let fresh = result.movies.filter { !seen.contains($0.id) }
                    if !fresh.isEmpty {
                        state.uiState = .loaded(current + fresh)
                        return
                    }
                }
            } catch {
                return
            }
        }
    }
}
