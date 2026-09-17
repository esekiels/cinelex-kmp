plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
	alias(libs.plugins.detekt)
}

subprojects {
	apply(plugin = "io.gitlab.arturbosch.detekt")
	
	detekt {
		config.from(rootProject.files("config/detekt/detekt.yml"))
		buildUponDefaultConfig = true
		parallel = true
		source.setFrom(files("src"))
	}
	
	dependencies {
		add("detektPlugins", rootProject.libs.detekt.formatting)
	}
}
