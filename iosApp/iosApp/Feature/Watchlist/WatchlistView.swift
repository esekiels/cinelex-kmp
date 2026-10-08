//
//  WatchlistView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 06/10/26.
//

import Shared
import SwiftUI

struct WatchlistView: View {

    @State private var viewModel: WatchlistViewModel
    @Environment(\.factory) private var factory
    @Namespace private var watchlistNs

    init(viewModel: WatchlistViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    var body: some View {
        NavigationStack {
            content
                .task { await viewModel.onAppear() }
                .navigationTitle(Text("watchlist"))
                .background(Color.background)
                .zoomDestination(for: Movie.self, in: watchlistNs) { movie in
                    if let factory {
                        DetailView(viewModel: factory.injectDetailViewModel(movieId: movie.id))
                    }
                }
        }
    }
}

private extension WatchlistView {

    @ViewBuilder
    var content: some View {
        switch viewModel.state {
        case .loaded(let movies):
            ScrollView(showsIndicators: false) {
                LazyVStack(alignment: .leading, spacing: 12) {
                    ForEach(movies) { movie in
                        MovieRow(movie: movie, source: AccessibilityID.watchlistRow, showsDetails: true)
                            .accessibilityIdentifier(AccessibilityID.watchlistRow)
                    }
                }
                .padding(16)
            }
        case .loading:
            Color.clear
        case .empty:
            ContentUnavailableView {
                Label("watchlist.empty", systemImage: "bookmark")
            } description: {
                Text("watchlist.emptyMessage")
            }
            .accessibilityIdentifier(AccessibilityID.watchlistEmpty)
        case .error(let code):
            ContentUnavailableView(
                "home.error",
                systemImage: "exclamationmark.triangle",
                description: Text(.error(code))
            )
        }
    }
}

#if DEBUG
#Preview("Saved") {
    WatchlistView(viewModel: WatchlistViewModel(repository: FakeWatchlistRepository(initial: MovieStubs.shared.all)))
}

#Preview("Empty") {
    WatchlistView(viewModel: WatchlistViewModel(repository: FakeWatchlistRepository()))
}
#endif
