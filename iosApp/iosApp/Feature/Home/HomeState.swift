//
//  HomeState.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import Shared

struct HomeState {
    
    var uiState: UiState<Carousels> = .loading
    
    struct Carousels {
        var nowPlaying: [Movie] = []
        var popular: [Movie] = []
        var upcoming: [Movie] = []
        var topRated: [Movie] = []
        
        var isEmpty: Bool {
            nowPlaying.isEmpty && popular.isEmpty && upcoming.isEmpty && topRated.isEmpty
        }
    }
}
