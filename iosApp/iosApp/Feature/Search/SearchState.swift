//
//  SearchState.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 04/10/26.
//

import Foundation
import Shared

struct SearchState {
    var uiState: UiState<[Movie]>?
    var recommendations: [Movie] = []
    var isLoadingMore = false
}
