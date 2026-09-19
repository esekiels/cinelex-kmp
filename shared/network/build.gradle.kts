import java.util.Properties

plugins {
	id("esekiels.cinelex.kmp.library")
	alias(libs.plugins.kotlinSerialization)
}

val tmdbToken: String =
	Properties().run {
		val file = rootProject.file("local.properties")
		if (file.exists()) file.inputStream().use { load(it) }
		getProperty("TMDB_TOKEN") ?: System.getenv("TMDB_TOKEN").orEmpty()
	}

val generateBuildKonfig by tasks.registering {
	val outputDir = layout.buildDirectory.dir("generated/buildkonfig/kotlin")
	val token = tmdbToken
	inputs.property("tmdbToken", token)
	outputs.dir(outputDir)
	doLast {
		val pkgDir = outputDir.get().asFile.resolve("co/esekiels/cinelex/network")
		pkgDir.mkdirs()
		pkgDir.resolve("BuildKonfig.kt").writeText(
			"""
			package co.esekiels.cinelex.network

			// GENERATED — do not edit. See shared/network/build.gradle.kts.
			internal object BuildKonfig {
			    const val BASE_URL: String = "https://api.themoviedb.org/3/"
			    const val TMDB_TOKEN: String = "$token"
			}

			""".trimIndent(),
		)
	}
}

kotlin {
	sourceSets {
		commonMain {
			kotlin.srcDir(generateBuildKonfig)
			dependencies {
				implementation(projects.shared.model)
				implementation(projects.shared.common)

				implementation(libs.ktor.client.core)
				implementation(libs.ktor.client.contentNegotiation)
				implementation(libs.ktor.serialization.json)
				implementation(libs.kotlinx.serialization.json)
				implementation(libs.ktor.client.logging)
			}
		}
		androidMain.dependencies {
			implementation(libs.ktor.client.okhttp)
		}
		iosMain.dependencies {
			implementation(libs.ktor.client.darwin)
		}
		commonTest.dependencies {
			implementation(libs.ktor.client.mock)
			implementation(libs.kotlinx.coroutines.test)
		}
	}
}
