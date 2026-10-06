import SwiftUI
import Shared

@main
struct iOSApp: App {

    private let factory: CinelexDIFactory
    @State private var preferences: PreferencesStore

    init() {
        let factory: CinelexDIFactory
        #if DEBUG
        if let scenario = UITestScenario.current {
            factory = CinelexDIFactory(
                movieRepository: scenario.repository,
                userDataRepository: FakeUserDataRepository(),
                watchlistRepository: FakeWatchlistRepository()
            )
        } else {
            KoinHelperKt.doInitKoin()
            factory = CinelexDIFactory()
        }
        #else
        KoinHelperKt.doInitKoin()
        factory = CinelexDIFactory()
        #endif
        self.factory = factory
        _preferences = State(initialValue: factory.injectPreferencesStore())
    }

    var body: some Scene {
        WindowGroup {
            AppTabView()
            .environment(\.factory, factory)
            .environment(preferences)
            .environment(\.locale, preferences.language.locale)
            .preferredColorScheme(preferences.theme.colorScheme)
            .tint(.colorPrimary)
        }
    }
}
