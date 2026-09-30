/*
 * Cinelex
 * DeviceLanguage.android
 *
 * Created by Esekiel Surbakti on 30/09/26
 */

package co.esekiels.cinelex.data

import java.util.Locale

internal actual fun deviceLanguageCode(): String? = Locale.getDefault().toLanguageTag().substringBefore('-')
