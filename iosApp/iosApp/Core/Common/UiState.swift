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
    case error(code: String, message: String)

    static func failed(_ error: Error) -> Self {
        let exception = error.cinelexException
        return .error(
            code: exception?.code ?? ErrorConstants.shared.UNKNOWN_ERROR,
            message: exception?.message ?? error.localizedDescription
        )
    }
}
