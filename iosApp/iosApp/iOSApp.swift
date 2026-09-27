import SwiftUI
import Shared

@main
struct iOSApp: App {

    private let repository: any MovieRepository
    @AppStorage(AppLanguage.storageKey) private var language = AppLanguage.preferred
    @AppStorage(AppTheme.storageKey) private var theme = AppTheme.system

    init() {
        #if DEBUG
        if let scenario = UITestScenario.current {
            UserDefaults.standard.removeObject(forKey: AppLanguage.storageKey)
            UserDefaults.standard.removeObject(forKey: AppTheme.storageKey)
            repository = scenario.repository
            return
        }
        #endif
        KoinHelperKt.doInitKoin()
        repository = KoinHelperKt.movieRepository()
    }

    var body: some Scene {
        WindowGroup {
            HomeView(viewModel: HomeViewModel(repository: repository))
                .environment(\.locale, language.locale)
                .preferredColorScheme(theme.colorScheme)
                .tint(.colorPrimary)
        }
    }
}
