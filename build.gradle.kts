plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
	alias(libs.plugins.detekt) apply false
	alias(libs.plugins.ktlint) apply false
	alias(libs.plugins.ksp) apply false
	alias(libs.plugins.room) apply false
    alias(libs.plugins.kmpNativeCoroutines) apply false
}
