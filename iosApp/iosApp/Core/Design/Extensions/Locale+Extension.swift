//
//  Locale+Extension.swift
//  iosApp
//

import Foundation

extension Locale {

    func localized(_ key: String.LocalizationValue) -> String {
        String(localized: LocalizedStringResource(key, locale: self))
    }
}
