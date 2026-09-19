/*
 * Cinelex
 * CinelexSDK
 *
 * Created by Esekiel Surbakti on 17/09/26
 */

package co.esekiels.cinelex

import co.esekiels.cinelex.common.platform

// linker framework so Xcode links one frame instead of all shared
object CinelexSDK {
	const val VERSION: String = "1.0.0"
	
	val platformName: String get() = platform().name
}
