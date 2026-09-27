//
//  ErrorMessage.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Shared
import SwiftUI

extension LocalizedStringKey {

    static func error(_ code: String) -> LocalizedStringKey {
        switch code {
        case ErrorConstants.shared.NETWORK_ERROR: "error.network"
        case ErrorConstants.shared.HTTP_UNAUTHORIZED: "error.unauthorized"
        case ErrorConstants.shared.HTTP_TIMEOUT: "error.timeout"
        case ErrorConstants.shared.HTTP_FORBIDDEN: "error.forbidden"
        default: "error.unknown"
        }
    }
}
