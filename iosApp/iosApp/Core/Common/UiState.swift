//
//  UiState.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation

enum UiState<T> {
    case loading
    case loaded(T)
    case empty
    case error(code: String, message: String)
}
