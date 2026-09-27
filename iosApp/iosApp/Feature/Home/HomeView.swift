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
    @AppStorage(AppLanguage.storageKey) private var language = AppLanguage.preferred
    @AppStorage(AppTheme.storageKey) private var theme = AppTheme.system
    @Environment(\.locale) private var locale
    
    init(viewModel: HomeViewModel) {
        _viewModel = State(initialValue: viewModel)
    }
    
    var body: some View {
        NavigationStack {
            content
                .task { viewModel.onAppear() }
                .refreshable { await viewModel.refresh() }
                .onDisappear { viewModel.onDisappear() }
                .navigationTitle(Text(verbatim: "Cinelex"))
                .toolbar {
                    ToolbarItem(placement: .topBarTrailing) { localeMenu.tint(.textPrimary) }
                    ToolbarItem(placement: .topBarTrailing) { themeMenu.tint(.textPrimary) }
                }
                .background(Color.background)
        }
    }
}

private extension HomeView {
    
    var localeMenu: some View {
        Menu {
            Picker(selection: $language) {
                ForEach(AppLanguage.allCases, id: \.self) { option in
                    Label(option.title, systemImage: option.icon)
                        .tag(option)
                }
            } label: {
                Text("language.title")
            }
        } label: {
            Image(systemName: language.icon)
                .accessibilityLabel(Text("action.language"))
        }
        .accessibilityIdentifier(AccessibilityID.localeMenu)
        .accessibilityValue(language.rawValue)
    }
    
    var themeMenu: some View {
        Menu {
            Picker(selection: $theme) {
                ForEach(AppTheme.allCases, id: \.self) { option in
                    Label(option.title, systemImage: option.icon)
                        .tag(option)
                }
            } label: {
                Text("theme.title")
            }
        } label: {
            Image(systemName: theme.icon)
                .accessibilityLabel(Text("action.theme"))
        }
        .accessibilityIdentifier(AccessibilityID.themeMenu)
        .accessibilityValue(theme.rawValue)
    }
    
    func localized(_ key: String.LocalizationValue) -> String {
        String(localized: LocalizedStringResource(key, locale: locale))
    }
    
    @ViewBuilder
    var content: some View {
        ScrollView(showsIndicators: false) {
            VStack(spacing: 24) {
                switch viewModel.state.uiState {
                case .loaded(let data):
                    PosterCarouselView(title: localized("home.nowPlaying"), data: data.nowPlaying)
                    BackdropCarouselView(title: localized("home.popular"), data: data.popular)
                    PosterCarouselView(title: localized("home.topRated"), data: data.topRated)
                    BackdropCarouselView(title: localized("home.upcoming"), data: data.upcoming)
                case .loading:
                    SkeletonView(style: .poster)
                    SkeletonView(style: .backdrop)
                    SkeletonView(style: .poster)
                    SkeletonView(style: .backdrop)
                case .empty:
                    ContentUnavailableView("home.empty", systemImage: "film")
                        .containerRelativeFrame(.vertical)
                        .accessibilityIdentifier(AccessibilityID.homeEmpty)
                case .error(let code, _):
                    ContentUnavailableView(
                        "home.error",
                        systemImage: "exclamationmark.triangle",
                        description: Text(.error(code))
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
