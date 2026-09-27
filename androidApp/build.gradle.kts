import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeCompiler)
	id("esekiels.cinelex.quality")
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_11
	}
}
dependencies {
	implementation(projects.shared.data)
	implementation(projects.shared.common)

	implementation(libs.koin.android)
	implementation(libs.coil.compose)
	implementation(libs.coil.network.okhttp)
	
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.lifecycle.runtimeCompose)
	implementation(libs.androidx.lifecycle.viewmodelCompose)
	
	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.compose.materialIconsExtended)
	implementation(libs.compose.uiToolingPreview)
	debugImplementation(libs.compose.uiTooling)

	testImplementation(projects.shared.testing)
	testImplementation(libs.kotlin.test.junit)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.robolectric)
	testImplementation(libs.compose.uiTest.junit4)
}

android {
	namespace = "co.esekiels.cinelex"
	compileSdk =
		libs.versions.android.compileSdk
			.get()
			.toInt()

	defaultConfig {
		applicationId = "co.esekiels.cinelex"
		minSdk =
			libs.versions.android.minSdk
				.get()
				.toInt()
		targetSdk =
			libs.versions.android.targetSdk
				.get()
				.toInt()
		versionCode = 1
		versionName = "1.0"
	}
	androidResources {
		localeFilters += listOf("en", "in")
		generateLocaleConfig = true
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro",
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	testOptions {
		unitTests.isIncludeAndroidResources = true
		// Robolectric on SDK 36 pokes FileDescriptor internals via SharedSecrets.
		unitTests.all { it.jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED") }
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
}
