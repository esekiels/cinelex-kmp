/*
 * Cinelex
 * PlatformTest
 *
 * Created by Esekiel Surbakti on 17/09/26
 */

package co.esekiels.cinelex.common

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {
	
	@Test
	fun platformNameIsNotBlank() {
		assertTrue(platform().name.isNotBlank())
	}
}
