plugins {
	id("esekiels.cinelex.kmp.library")
	alias(libs.plugins.kmpNativeCoroutines)
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			api(projects.shared.model)
			api(projects.shared.common)
			
			// kept off the apps' compile classpath
			implementation(projects.shared.network)
			implementation(projects.shared.database)
			implementation(projects.shared.datastore)
			implementation(libs.kotlinx.coroutines.core)
		}
		// Repository tests are integration tests: real Room, MockEngine for HTTP.
		// Room's no-arg in-memory builder is native-only, so they live in iosTest.
		iosTest.dependencies {
			implementation(libs.kotlinx.coroutines.test)
			implementation(libs.ktor.client.mock)
			implementation(libs.room.runtime)
			implementation(libs.sqlite.bundled)
		}
	}
}
