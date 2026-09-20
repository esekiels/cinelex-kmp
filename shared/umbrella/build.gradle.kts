import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

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
			// stubs are for previews/tests only: exported to Swift in debug, dead-stripped in release.
			if (buildType == NativeBuildType.DEBUG) {
				export(projects.shared.testing)
			}
		}
	}
	
	sourceSets {
		commonMain.dependencies {
			api(projects.shared.data)
			api(projects.shared.model)
			api(projects.shared.common)
			api(projects.shared.testing)
		}
	}
}
