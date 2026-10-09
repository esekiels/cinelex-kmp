import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import co.esekiels.cinelex.libs
import co.esekiels.cinelex.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
	
	override fun apply(target: Project) = with(target) {
		pluginManager.apply("org.jetbrains.kotlin.multiplatform")
		pluginManager.apply("com.android.kotlin.multiplatform.library")
		pluginManager.apply("esekiels.cinelex.quality")
		
		extensions.configure<KotlinMultiplatformExtension> {
			(this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
				namespace = "co.esekiels.cineylex.shared.${target.name}"
				compileSdk = libs.version("android-compileSdk").toInt()
				minSdk = libs.version("android-minSdk").toInt()
				compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
			}
			
			iosArm64()
			iosSimulatorArm64()
			
			sourceSets.matching { it.name.startsWith("ios") }.configureEach {
				languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
			}

			sourceSets.getByName("commonTest").dependencies {
				implementation(kotlin("test"))
			}
		}
	}
}
