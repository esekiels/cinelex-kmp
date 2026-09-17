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
		
		extensions.configure<KotlinMultiplatformExtension> {
			// The Android target's DSL is nested inside `kotlin { }` with AGP's
			// KMP library plugin, so it's an extension *of the Kotlin extension*.
			(this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
				// Derived from the module name: :shared:network -> ...shared.network
				namespace = "co.esekiels.cinelex.shared.${target.name}"
				compileSdk = libs.version("android-compileSdk").toInt()
				minSdk = libs.version("android-minSdk").toInt()
				compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
				
				withHostTest { }
			}
			
			iosArm64()
			iosSimulatorArm64()
			
			compilerOptions {
				// Module-wide opt-ins only. A marker for a library the module
				// does not depend on is a warning on every compile.
				freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
			}
			
			sourceSets.getByName("commonTest").dependencies {
				implementation(kotlin("test"))
			}
		}
	}
}
