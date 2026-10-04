//
//  SearchView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import Kingfisher
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
        case .error(let code, _):
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
                    row(movie, source: rowID, showsDetails: showsDetails)
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

    /// `showsDetails` is off for recommendations: the cached list keeps neither rating nor release date.
    func row(_ movie: Movie, source: String, showsDetails: Bool) -> some View {
        ZoomLink(movie, source: source) {
            HStack(spacing: 12) {
                KFImage.url(movie.posterURL)
                    .placeholder { ImagePlaceholder(2 / 3) }
                    .resizable()
                    .cacheOriginalImage()
                    .scaleFactor(UIScreen.main.scale)
                    .fade(duration: 0.2)
                    .aspectRatio(2 / 3, contentMode: .fit)
                    .frame(width: 60)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                VStack(alignment: .leading, spacing: 6) {
                    Text(movie.title)
                        .font(.headline)
                        .foregroundStyle(.textPrimary)
                        .lineLimit(2)
                    if showsDetails {
                        if !movie.releaseDate.isEmpty {
                            Text(verbatim: String(movie.releaseDate.prefix(4)))
                                .font(.subheadline)
                                .foregroundStyle(.textSecondary)
                        }
                        Label {
                            Text(verbatim: movie.rating)
                        } icon: {
                            Image(systemName: "star.fill").foregroundStyle(.yellow)
                        }
                        .font(.subheadline)
                        .foregroundStyle(.textSecondary)
                    }
                }
                Spacer(minLength: 0)
            }
            .contentShape(Rectangle())
        }
        .accessibilityLabel(movie.title)
    }
}

#if DEBUG
#Preview("Recommendations") {
    let repository = FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
    return SearchView(viewModel: SearchViewModel(repository: repository))
        .environment(\.factory, CinelexDIFactory(movieRepository: repository, userDataRepository: FakeUserDataRepository()))
}
#endif
