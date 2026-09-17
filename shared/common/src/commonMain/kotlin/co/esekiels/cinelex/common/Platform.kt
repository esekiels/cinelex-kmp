/*
 * Cinelex
 * Platform
 *
 * Created by Esekiel Surbakti on 17/09/26
 */

package co.esekiels.cinelex.common

interface Platform {
	val name: String
}

expect fun platform(): Platform
