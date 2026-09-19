plugins {
	id("esekiels.cinelex.kmp.library")
	alias(libs.plugins.kotlinSerialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.room)
}

room {
	schemaDirectory("$projectDir/schemas")
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			implementation(projects.shared.model)
			implementation(projects.shared.common)

			implementation(libs.room.runtime)
			implementation(libs.sqlite.bundled)
			implementation(libs.kotlinx.coroutines.core)
			implementation(libs.kotlinx.serialization.json)
		}
		commonTest.dependencies {
			implementation(libs.kotlinx.coroutines.test)
			implementation(projects.shared.model)
		}
		iosTest.dependencies {
			implementation(libs.kotlinx.coroutines.test)
			implementation(projects.shared.model)
		}
	}
}

dependencies {
	add("kspAndroid", libs.room.compiler)
	add("kspIosArm64", libs.room.compiler)
	add("kspIosSimulatorArm64", libs.room.compiler)
}
