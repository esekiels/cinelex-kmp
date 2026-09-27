//
//  AppLanguage.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Shared
import SwiftUI

enum AppLanguage: String, CaseIterable {
    case en
    case id

    static var preferred: Self {
        Locale.preferredLanguages.first?.hasPrefix("id") == true ? .id : .en
    }

    var title: LocalizedStringKey {
        switch self {
        case .en: "language.english"
        case .id: "language.indonesian"
        }
    }

    var icon: String {
        switch self {
        case .en: "e.circle"
        case .id: "i.circle"
        }
    }

    var locale: Locale {
        Locale(identifier: rawValue)
    }

    var language: Language {
        switch self {
        case .en: .english
        case .id: .indonesian
        }
    }

    init(language: Language) {
        self = language == .indonesian ? .id : .en
    }
}
