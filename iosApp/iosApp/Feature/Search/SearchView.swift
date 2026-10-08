//
//  SearchView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import Shared
import SwiftUI

struct SearchView: View {

    @State private var viewModel: SearchViewModel
    @Environment(\.factory) private var factory
    @Namespace private var searchNs

    init(viewModel: SearchViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    var body: some View {
        NavigationStack {
            content
                .task { await viewModel.onAppear() }
                .task(id: viewModel.query) { await viewModel.search() }
                .searchable(text: $viewModel.query, prompt: Text("search.prompt"))
                .navigationTitle(Text("search"))
                .background(Color.background)
                .zoomDestination(for: Movie.self, in: searchNs) { movie in
                    if let factory {
                        DetailView(viewModel: factory.injectDetailViewModel(movieId: movie.id))
                    }
                }
        }
    }
}

private extension SearchView {

    @ViewBuilder
    var content: some View {
        switch viewModel.isQueryEmpty ? nil : viewModel.state.uiState {
        case nil:
            list(viewModel.state.recommendations, rowID: AccessibilityID.recommendationRow) {
                Text("search.recommendations")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundStyle(.textPrimary)
            }
        case .loaded(let movies):
            list(
                movies,
                rowID: AccessibilityID.searchResultRow,
                showsDetails: true,
                onRowAppear: viewModel.loadMoreIfNeeded
            ) {
                EmptyView()
            }
        case .loading:
            ProgressView()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        case .empty:
            ContentUnavailableView.search(text: viewModel.query)
                .accessibilityIdentifier(AccessibilityID.searchEmpty)
        case .error(let code):
            ContentUnavailableView(
                "search.error",
                systemImage: "exclamationmark.triangle",
                description: Text(.error(code))
            )
            .accessibilityIdentifier(AccessibilityID.searchError)
        }
    }

    func list(
        _ movies: [Movie],
        rowID: String,
        showsDetails: Bool = false,
        onRowAppear: @escaping (Movie) -> Void = { _ in },
        @ViewBuilder header: () -> some View
    ) -> some View {
        ScrollView(showsIndicators: false) {
            LazyVStack(alignment: .leading, spacing: 12) {
                header()
                ForEach(movies) { movie in
                    MovieRow(movie: movie, source: rowID, showsDetails: showsDetails)
                        .accessibilityIdentifier(rowID)
                        .onAppear { onRowAppear(movie) }
                }
                if viewModel.state.isLoadingMore {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                }
            }
            .padding(16)
        }
        .scrollDismissesKeyboard(.immediately)
    }
}

#if DEBUG
#Preview("Recommendations") {
    let repository = FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
    return SearchView(viewModel: SearchViewModel(repository: repository))
        .environment(
            \.factory,
            CinelexDIFactory(
                movieRepository: repository,
                userDataRepository: FakeUserDataRepository(),
                watchlistRepository: FakeWatchlistRepository()
            )
        )
}
#endif
