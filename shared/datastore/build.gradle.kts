plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			implementation(projects.shared.model)

			implementation(libs.datastore.preferences.core)
			implementation(libs.okio)
			implementation(libs.kotlinx.coroutines.core)

			api(libs.koin.core)
		}

		androidMain.dependencies {
			implementation(libs.koin.android)
		}

		iosTest.dependencies {
			implementation(libs.kotlinx.coroutines.test)
		}
	}
}
