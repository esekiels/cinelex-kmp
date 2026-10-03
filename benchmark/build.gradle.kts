import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.androidTest)
	alias(libs.plugins.baselineProfile)
	id("esekiels.cinelex.quality")
}

kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_11 } }

android {
	namespace = "co.esekiels.cinelex.benchmark"
	compileSdk =
		libs.versions.android.compileSdk
			.get()
			.toInt()

	defaultConfig {
		minSdk =
			libs.versions.android.minSdk
				.get()
				.toInt()
		targetSdk =
			libs.versions.android.targetSdk
				.get()
				.toInt()
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

	targetProjectPath = ":androidApp"
	experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
	useConnectedDevices = true
}

dependencies {
	implementation(libs.androidx.benchmark.macro)
	implementation(libs.androidx.uiautomator)
	implementation(libs.androidx.test.junit)
}
