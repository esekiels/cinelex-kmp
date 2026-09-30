/*
 * Cinelex
 * CinelexException
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.common

class CinelexException(
	val code: String,
	override val message: String,
	cause: Throwable?,
) : Exception(message, cause) {
	constructor(code: String, message: String) : this(code, message, null)
}
