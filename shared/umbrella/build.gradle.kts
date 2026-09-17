plugins {
	id("esekiels.cinelex.kmp.library")
}

kotlin {
	
	listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "Shared"
			isStatic = true
			// makes dependency's types VISIBLE to swift.
			export(projects.shared.data)
			export(projects.shared.model)
			export(projects.shared.common)
		}
	}
	
	sourceSets {
		commonMain.dependencies {
			api(projects.shared.data)
			api(projects.shared.model)
			api(projects.shared.common)
		}
	}
}
