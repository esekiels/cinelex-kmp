plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			api(projects.shared.data)
			implementation(libs.kotlinx.coroutines.core)
		}
	}
}
