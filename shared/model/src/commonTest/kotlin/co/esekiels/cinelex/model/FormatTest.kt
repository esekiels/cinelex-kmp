/*
 * Cinelex
 * FormatTest
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.model

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatTest {

    @Test
    fun formatsOneDecimal() {
        assertEquals("7.5", 7.5.toOneDecimal())
        assertEquals("8.0", 8.0.toOneDecimal())
        assertEquals("0.0", 0.0.toOneDecimal())
    }

    @Test
    fun roundsToNearestTenth() {
        assertEquals("7.6", 7.55.toOneDecimal())
        assertEquals("7.5", 7.549.toOneDecimal())
        assertEquals("10.0", 9.99.toOneDecimal())
    }

    @Test
    fun handlesNegative() {
        assertEquals("-1.5", (-1.5).toOneDecimal())
    }
}
