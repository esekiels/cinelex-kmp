/*
 * Cinelex
 * Platform
 *
 * Created by Esekiel Surbakti on 17/09/26
 */

package co.esekiels.cinelex.common

import android.os.Build

private class AndroidPlatform : Platform {
	override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun platform(): Platform = AndroidPlatform()
