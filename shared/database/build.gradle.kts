plugins {
	id("esekiels.cinelex.kmp.library")
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

			implementation(libs.room.runtime)
			implementation(libs.sqlite.bundled)
			implementation(libs.kotlinx.coroutines.core)
			implementation(libs.kotlinx.serialization.json)

			api(libs.koin.core)
		}

		androidMain.dependencies {
			implementation(libs.koin.android)
		}

		commonTest.dependencies {
			implementation(libs.kotlinx.coroutines.test)
		}
	}
}

dependencies {
	add("kspAndroid", libs.room.compiler)
	add("kspIosArm64", libs.room.compiler)
	add("kspIosSimulatorArm64", libs.room.compiler)
}
