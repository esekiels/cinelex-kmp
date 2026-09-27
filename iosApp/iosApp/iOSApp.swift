import SwiftUI
import Shared

@main
struct iOSApp: App {

    private let repository: any MovieRepository
    @State private var preferences: PreferencesStore

    init() {
        #if DEBUG
        if let scenario = UITestScenario.current {
            repository = scenario.repository
            _preferences = State(initialValue: PreferencesStore(repository: FakeUserDataRepository()))
            return
        }
        #endif
        KoinHelperKt.doInitKoin()
        repository = KoinHelperKt.movieRepository()
        _preferences = State(initialValue: PreferencesStore(repository: KoinHelperKt.userDataRepository()))
    }

    var body: some Scene {
        WindowGroup {
            HomeView(viewModel: HomeViewModel(repository: repository))
                .environment(preferences)
                .environment(\.locale, preferences.language.locale)
                .preferredColorScheme(preferences.theme.colorScheme)
                .tint(.colorPrimary)
        }
    }
}
