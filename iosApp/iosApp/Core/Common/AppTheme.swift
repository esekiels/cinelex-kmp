//
//  AppTheme.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Shared
import SwiftUI

enum AppTheme: String, CaseIterable {
    case system
    case light
    case dark

    var title: LocalizedStringKey {
        switch self {
        case .system: "theme.system"
        case .light: "theme.light"
        case .dark: "theme.dark"
        }
    }

    var icon: String {
        switch self {
        case .system: "circle.lefthalf.filled"
        case .light: "sun.max"
        case .dark: "moon"
        }
    }

    var colorScheme: ColorScheme? {
        switch self {
        case .system: nil
        case .light: .light
        case .dark: .dark
        }
    }

    var uiTheme: UiTheme {
        switch self {
        case .system: .followSystem
        case .light: .light
        case .dark: .dark
        }
    }

    init(uiTheme: UiTheme) {
        switch uiTheme {
        case .light: self = .light
        case .dark: self = .dark
        default: self = .system
        }
    }
}
