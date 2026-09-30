# Cinelex

A movie browser for Android (Jetpack Compose) and iOS (SwiftUI) built on the TMDB API. The data, caching and preferences layers are shared through Kotlin Multiplatform, and each platform keeps native UI.

## Layout

| Path | What lives there |
|---|---|
| `androidApp/` | Compose UI, Navigation3, Koin view models, Robolectric UI tests |
| `iosApp/` | SwiftUI app and XCUITests (`CinelexUITests`) |
| `shared/model` | Domain models (`Movie`, `MovieDetails`, preferences) |
| `shared/network` | Ktor client for TMDB, error mapping into `CinelexException` |
| `shared/database` | Room cache for carousels and movie details |
| `shared/datastore` | DataStore-backed user preferences (theme, language) |
| `shared/data` | Repositories combining network, cache and preferences; Koin wiring |
| `shared/common` | `CinelexException`, error codes, dispatchers |
| `shared/testing` | Fakes and stubs used by unit tests, UI tests and previews |
| `shared/umbrella` | The `Shared` framework exported to iOS |
| `build-logic/` | Gradle convention plugins (KMP library, quality checks) |

## Setup

Add a TMDB v4 read access token to `local.properties` (it is git-ignored), or export it as `TMDB_TOKEN`:

```properties
TMDB_TOKEN=eyJhbGciOi...
```

The build fails without it.

## Run

- Android: `./gradlew :androidApp:installDebug`
- iOS: open `iosApp/iosApp.xcodeproj` in Xcode and run the `iosApp` scheme. The shared framework is built by the Xcode build phase.

## Test

- Everything on the JVM side (unit tests, Android UI tests, detekt, ktlint): `./gradlew check`
- Shared tests on the iOS simulator: `./gradlew :shared:data:iosSimulatorArm64Test :shared:database:iosSimulatorArm64Test`
- iOS UI tests: `xcodebuild test -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17 Pro' -only-testing:CinelexUITests`
