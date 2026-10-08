//
//  UiState.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import Shared

enum UiState<T> {
    case loading
    case loaded(T)
    case empty
    case error(code: String)

    static func failed(_ error: Error) -> Self {
        .error(code: error.cinelexException?.code ?? ErrorConstants.shared.UNKNOWN_ERROR)
    }
}
