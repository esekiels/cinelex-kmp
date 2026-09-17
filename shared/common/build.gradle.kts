plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			implementation(libs.kotlinx.coroutines.core)
		}
	}
}
