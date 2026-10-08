# Cinelex

A movie browser for Android (Jetpack Compose) and iOS (SwiftUI) built on the TMDB API. The data, caching and preferences layers are shared through Kotlin Multiplatform, and each platform keeps native UI.

Features: home carousels (now playing, upcoming, top rated, popular), search, movie details, a local watchlist, and theme/language preferences (English, Indonesian).

## Architecture

```
 androidApp (Compose, ViewModel, Koin)          iosApp (SwiftUI, @Observable ViewModels)
  │    ┊ tests                                            │
  │    ┊                                   shared/umbrella → Shared.framework
  │    ▼              exported in debug builds only       │   │
  │  shared/testing ◄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤   │
  │  (fake repositories)                                  │   │
  │    │                                                  │   │
  └────┴──────────────────┬───────────────────────────────┘   │
                          ▼                                   │
                     shared/data   Movie/Watchlist/UserData   │
                          │        repositories + Koin modules│
        ┌─────────────────┼──────────────────┐                │
        ▼                 ▼                  ▼                │
  shared/network    shared/database    shared/datastore       │
  (Ktor → TMDB)     (Room cache)       (DataStore prefs)      │
        └─────────────────┼──────────────────┘                │
                          ▼                                   │
                       shared/model  ◄────────────────────────┘
```

`shared/testing` fakes implement the `shared/data` repository interfaces. `shared/data` exposes `model` as `api`, so `androidApp` reaches `model` through it; the umbrella exports it to Swift directly (right rail).

- **Offline-first repositories.** Carousels (`observeMovies(MovieCategory)`) and details are observed from Room via `Flow`; `refresh*` calls fetch from TMDB and write into the database, so the cache is the single source of truth. Cached content is keyed by language (preference, then device language, then English) and re-queried when it changes. Search is network-only; the watchlist is a local snapshot keyed by movie id.
- **One error type.** Network, database and preference storage failures are mapped to `CinelexException` with an error code. Both apps show a localized message chosen by that code, never the raw exception text.
- **Swift interop.** `shared/data` uses KMP-NativeCoroutines so Swift consumes flows and suspend functions as `AsyncSequence` / `async`. `shared/umbrella` exports `data`, `model` (and `testing` in debug builds) into a static `Shared` framework.
- **Android UI.** Feature packages (`home`, `search`, `detail`, `watchlist`) with a stateful screen and stateless content composable, ViewModels exposing `StateFlow`, Navigation3 for routing and Koin for injection.
- **iOS UI.** Matching `Feature/` folders with `@Observable @MainActor` ViewModels, wired through `CinelexDIFactory` in the SwiftUI environment, which resolves repositories from Koin via `KoinHelper`.
- **Shared fakes.** `shared/testing` holds fake repositories used by Android Robolectric tests, SwiftUI previews and XCUITests, so both apps are tested against the same data. Android test tags and iOS accessibility IDs use the same strings where both platforms have the element.

## Layout

| Path | What lives there |
|---|---|
| `androidApp/` | Compose UI, Navigation3, Koin view models, Robolectric UI tests |
| `iosApp/` | SwiftUI app, XCUITests (`CinelexUITests`), test plans, SwiftLint config |
| `benchmark/` | Android macrobenchmarks and baseline profile generator |
| `shared/model` | Domain models (`Movie`, `MovieDetails`, `MovieCategory`, preferences), `CinelexException` and error codes |
| `shared/network` | Ktor client for TMDB, error mapping into `CinelexException` |
| `shared/database` | Room cache for carousels, movie details and the watchlist |
| `shared/datastore` | DataStore-backed user preferences (theme, language) |
| `shared/data` | Repositories combining network, cache and preferences; Koin wiring |
| `shared/testing` | Fakes and stubs used by unit tests, UI tests and previews |
| `shared/umbrella` | The `Shared` framework exported to iOS |
| `build-logic/` | Gradle convention plugins: `esekiels.cinelex.kmp.library` (KMP + Android library + iOS targets) and `esekiels.cinelex.quality` (detekt, ktlint) |
| `config/detekt/` | Detekt rules |

## Setup

Requirements: JDK 21 for the Gradle daemon (auto-provisioned via `gradle/gradle-daemon-jvm.properties`), Android SDK 36 (min SDK 24), Xcode with an iOS 18.2+ simulator, optionally `swiftlint` (run by an Xcode build phase when installed).

Add a TMDB v4 read access token to `local.properties` (it is git-ignored), or export it as `TMDB_TOKEN`. The key is case-sensitive:

```properties
TMDB_TOKEN=eyJhbGciOi...
```

The build fails without it.

## Build and run

| Task | Command |
|---|---|
| Android debug install | `./gradlew :androidApp:installDebug` |
| Android release APK | `./gradlew :androidApp:assembleRelease` |
| iOS framework only | `./gradlew :shared:umbrella:linkDebugFrameworkIosSimulatorArm64` |
| iOS app | open `iosApp/iosApp.xcodeproj` and run the `iosApp` scheme (the build phase builds the shared framework) |
| iOS from CLI | `xcodebuild build -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17 Pro'` |

## Test

| What | Command |
|---|---|
| All JVM checks (unit tests, Android ViewModel and UI tests, detekt, ktlint) | `./gradlew check` |
| Android ViewModel and UI tests only | `./gradlew :androidApp:testDebugUnitTest` |
| Lint / format | `./gradlew detekt ktlintCheck` (`ktlintFormat` to fix) |
| Shared tests on iOS simulator (repositories, Room DAOs, DataStore) | `./gradlew :shared:data:iosSimulatorArm64Test :shared:database:iosSimulatorArm64Test :shared:datastore:iosSimulatorArm64Test` |
| iOS UI tests (`Functional` test plan) | `xcodebuild test -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17 Pro'` |
| iOS performance tests (`Performance` test plan) | `xcodebuild test -project iosApp/iosApp.xcodeproj -scheme Performance -destination 'platform=iOS Simulator,name=iPhone 17 Pro'` |
| Android macrobenchmarks (device required) | `./gradlew :benchmark:connectedCheck` |
| Regenerate baseline profile (device required) | `./gradlew :androidApp:generateReleaseBaselineProfile` |

### Test layers

- **Shared unit tests** (`commonTest`): mappers, plus the TMDB client and response mapping against Ktor `MockEngine`; `kotlin.test` + `runTest`.
- **Shared integration tests** (`iosTest`): repositories against a real in-memory Room database and Ktor `MockEngine` (`shared/data`), DAO tests (`shared/database`) and preference storage tests (`shared/datastore`).
- **Android tests** (`androidApp/src/test`): `feature/` holds ViewModel unit tests on the shared fakes; `journey/` is Robolectric + Compose UI tests driving user flows through page objects in `journey/screens`.
- **iOS UI tests** (`CinelexUITests`): XCUITest with screen objects in `Screens/`, launched with `-UITestStubs -UITestScenario <loaded|loading|empty|error|detailError>` (debug builds only) so the app runs on the Kotlin fakes from `shared/testing`. The `Functional` plan skips `PerformanceTests`; the `Performance` plan runs only those.
- **Performance**: `benchmark/` (startup, home scroll, detail) on Android; `CinelexUITests/Performance` on iOS.
