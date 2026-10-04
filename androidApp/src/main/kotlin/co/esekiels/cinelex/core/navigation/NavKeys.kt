/*
 * Cinelex
 * NavKeys
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data object Home : NavKey

@Serializable
internal data class Detail(
	val movieId: Int,
	val title: String,
	val source: String,
) : NavKey
