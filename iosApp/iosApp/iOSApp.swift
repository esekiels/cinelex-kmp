import SwiftUI
import Shared

@main
struct iOSApp: App {

    private let repository: any MovieRepository

    init() {
        #if DEBUG
        if let scenario = UITestScenario.current {
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
        }
    }
}
