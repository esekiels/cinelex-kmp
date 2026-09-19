/*
 * Cinelex
 * Format
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.model

import kotlin.math.abs
import kotlin.math.roundToLong

private const val TENTHS = 10

internal fun Double.toOneDecimal(): String {
	val scaled = (this * TENTHS).roundToLong()
	val sign = if (scaled < 0) "-" else ""
	val whole = abs(scaled) / TENTHS
	val tenth = abs(scaled) % TENTHS
	return "$sign$whole.$tenth"
}
