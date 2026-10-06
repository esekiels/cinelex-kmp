//
//  AppTabView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import SwiftUI

struct AppTabView: View {
    
    @Environment(\.factory) private var factory
    @Environment(\.locale) private var locale
    
    var body: some View {
        TabView {
            if let factory {
                HomeView(viewModel: factory.injectHomeViewModel())
                    .tabItem {
                        Label(locale.localized("home"), systemImage: "house.fill")
                    }
                SearchView(viewModel: factory.injectSearchViewModel())
                    .tabItem {
                        Label(locale.localized("search"), systemImage: "magnifyingglass")
                    }
                WatchlistView(viewModel: factory.injectWatchlistViewModel())
                    .tabItem {
                        Label(locale.localized("watchlist"), systemImage: "bookmark.fill")
                    }
            }
        }
    }
}
