//
//  HomeView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//
import SwiftUI
import Shared

enum CarouselStyle {
    case poster
    case backdrop
}

struct HomeView: View {
    
    @State private var viewModel: HomeViewModel
    
    init(viewModel: HomeViewModel) {
        _viewModel = State(initialValue: viewModel)
    }
    
    var body: some View {
        NavigationStack {
            content
                .task { viewModel.onAppear() }
                .refreshable { await viewModel.refresh() }
                .onDisappear { viewModel.onDisappear() }
        }
    }
}

private extension HomeView {
    
    @ViewBuilder
    var content: some View {
        ScrollView(showsIndicators: false) {
            VStack(spacing: 24) {
                switch viewModel.state.uiState {
                case .loaded(let data):
                    PosterCarouselView(title: "Now Playing", data: data.nowPlaying)
                    BackdropCarouselView(title: "Popular", data: data.popular)
                    PosterCarouselView(title: "Top Rated", data: data.topRated)
                    BackdropCarouselView(title: "Upcoming", data: data.upcoming)
                case .loading:
                    SkeletonView(style: .poster)
                    SkeletonView(style: .backdrop)
                    SkeletonView(style: .poster)
                    SkeletonView(style: .backdrop)
                case .empty:
                    ContentUnavailableView("No movies", systemImage: "film")
                        .containerRelativeFrame(.vertical)
                        .accessibilityIdentifier(AccessibilityID.homeEmpty)
                case .error(_, let message):
                    ContentUnavailableView(
                        "Something went wrong",
                        systemImage: "exclamationmark.triangle",
                        description: Text(message)
                    )
                    .containerRelativeFrame(.vertical)
                    .accessibilityIdentifier(AccessibilityID.homeError)
                }
            }
        }
    }
}

#if DEBUG
#Preview("Loaded") {
    let repository = FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
    return HomeView(viewModel: HomeViewModel(repository: repository))
}

#Preview("Loading") {
    let repository = FakeMovieRepository(nowPlaying: [], isLoading: true)
    return HomeView(viewModel: HomeViewModel(repository: repository))
}

#Preview("Empty") {
    let repository = FakeMovieRepository(nowPlaying: [])
    return HomeView(viewModel: HomeViewModel(repository: repository))
}

#Preview("Error") {
    let repository = FakeMovieRepository(nowPlaying: [])
    repository.failure = CinelexException(code: ErrorConstants.shared.NETWORK_ERROR, message: "Server unreachable")
    return HomeView(viewModel: HomeViewModel(repository: repository))
}
#endif
