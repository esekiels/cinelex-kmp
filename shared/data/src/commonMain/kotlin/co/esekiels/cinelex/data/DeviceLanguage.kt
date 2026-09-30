/*
 * Cinelex
 * DeviceLanguage
 *
 * Created by Esekiel Surbakti on 30/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UserPreferences

internal expect fun deviceLanguageCode(): String?

internal fun UserPreferences.contentLanguage(): Language {
	val device = Language.fromCode(deviceLanguageCode())
	return language ?: device ?: Language.ENGLISH
}
