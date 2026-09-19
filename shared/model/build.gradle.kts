plugins {
	id("esekiels.cinelex.kmp.library")
	alias(libs.plugins.kotlinSerialization)
}

kotlin {
	sourceSets {
		commonMain.dependencies {
			implementation(libs.kotlinx.serialization.json)
		}
	}
}
