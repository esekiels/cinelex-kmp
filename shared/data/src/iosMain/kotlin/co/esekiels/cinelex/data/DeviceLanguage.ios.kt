/*
 * Cinelex
 * DeviceLanguage.ios
 *
 * Created by Esekiel Surbakti on 30/09/26
 */

package co.esekiels.cinelex.data

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

internal actual fun deviceLanguageCode(): String? {
	val tag = NSLocale.preferredLanguages.firstOrNull() as? String
	return tag?.substringBefore('-')
}
