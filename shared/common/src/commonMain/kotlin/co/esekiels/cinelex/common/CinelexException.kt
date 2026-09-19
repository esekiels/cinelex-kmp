/*
 * Cinelex
 * CinelexException
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.common

data class CinelexException(
	val code: String,
	override val message: String,
) : Exception()
