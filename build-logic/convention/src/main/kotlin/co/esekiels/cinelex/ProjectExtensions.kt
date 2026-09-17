package co.esekiels.cinelex

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/** A convention plugin can't use the generated `libs` accessor — it resolves the catalog by name. */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.version(name: String): String =
    findVersion(name).get().requiredVersion
