//
//  PreferencesStore.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Foundation
import KMPNativeCoroutinesAsync
import KMPNativeCoroutinesCore
import Shared

@Observable
@MainActor
final class PreferencesStore {

    private(set) var language = AppLanguage.preferred
    private(set) var theme = AppTheme.system

    private let repository: any UserDataRepository
    private var lastWrite: Task<Void, Never>?

    init(repository: any UserDataRepository) {
        self.repository = repository
        apply(KoinHelperKt.currentUserPreferences(repo: repository))
        observe()
    }

    func setLanguage(_ language: AppLanguage) {
        write { try await asyncFunction(for: self.repository.setLanguage(language: language.language)) }
    }

    func setTheme(_ theme: AppTheme) {
        write { try await asyncFunction(for: self.repository.setUiTheme(uiTheme: theme.uiTheme)) }
    }

    private func write(_ operation: @escaping () async throws -> Void) {
        lastWrite = Task { [previous = lastWrite] in
            await previous?.value
            try? await operation()
        }
    }

    private func apply(_ preferences: UserPreferences) {
        language = preferences.language.map(AppLanguage.init) ?? .preferred
        theme = AppTheme(uiTheme: preferences.uiTheme)
    }

    private func observe() {
        Task {
            do {
                for try await preferences in asyncSequence(for: repository.userPreferences) {
                    apply(preferences)
                }
            } catch {
            }
        }
    }
}
