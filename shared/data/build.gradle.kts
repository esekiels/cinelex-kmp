plugins {
	id("esekiels.cinelex.kmp.library")
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
	}
}
