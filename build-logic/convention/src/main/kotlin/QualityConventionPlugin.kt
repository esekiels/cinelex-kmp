import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class QualityConventionPlugin : Plugin<Project> {
	
	override fun apply(target: Project) = with(target) {
		pluginManager.apply("io.gitlab.arturbosch.detekt")
		pluginManager.apply("org.jlleitschuh.gradle.ktlint")
		
		extensions.configure<DetektExtension> {
			config.from(rootProject.file("config/detekt/detekt.yml"))
			buildUponDefaultConfig = true
			parallel = true
			source.setFrom(files("src"))
		}
		
		extensions.configure<KtlintExtension> {
			// Generated sources (e.g. BuildKonfig) aren't ours to format.
			filter { exclude { it.file.path.contains("/build/") } }
		}
	}
}
