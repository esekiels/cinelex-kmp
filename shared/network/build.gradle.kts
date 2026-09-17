plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			implementation(projects.shared.model)
			implementation(projects.shared.common)
		}
	}
}
