plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	sourceSets {
		commonMain.dependencies {

			api(projects.shared.data)
			api(projects.shared.model)
			implementation(libs.kotlinx.coroutines.core)
		}
	}
}
