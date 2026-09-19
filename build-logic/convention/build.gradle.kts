import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	`kotlin-dsl`
}

group = "co.esekiels.cinelex.buildlogic"

java {
	toolchain { languageVersion = JavaLanguageVersion.of(17) }
}

kotlin {
	compilerOptions { jvmTarget = JvmTarget.JVM_17 }
}

dependencies {
	// compileOnly: these plugins are on the consuming build's classpath at runtime,
	// we only need their types to compile against.
	compileOnly(libs.android.gradlePlugin)
	compileOnly(libs.kotlin.gradlePlugin)
	compileOnly(libs.ksp.gradlePlugin)
	compileOnly(libs.detekt.gradlePlugin)
	compileOnly(libs.ktlint.gradlePlugin)
}

gradlePlugin {
	plugins {
		register("kmpLibrary") {
			id = "esekiels.cinelex.kmp.library"
			implementationClass = "KmpLibraryConventionPlugin"
		}
		register("quality") {
			id = "esekiels.cinelex.quality"
			implementationClass = "QualityConventionPlugin"
		}
	}
}
