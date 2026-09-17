/*
 * Cinelex
 * Platform
 *
 * Created by Esekiel Surbakti on 17/09/26
 */

package co.esekiels.cinelex.common

import platform.UIKit.UIDevice

private class IOSPlatform : Platform {
	override val name: String =
		UIDevice.currentDevice.systemName + " " + UIDevice.currentDevice.systemVersion
}

actual fun platform(): Platform = IOSPlatform()
