/*
 * Cinelex
 * BaselineProfileGenerator
 *
 * Created by Esekiel Surbakti on 03/10/26
 */

package co.esekiels.cinelex.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates the baseline profile: `./gradlew :androidApp:generateReleaseBaselineProfile`.
 * Requires API 33+ or a rooted API 28+ device.
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
	@get:Rule
	val rule = BaselineProfileRule()

	/** Startup path only, so dex layout optimisation targets what runs at launch. */
	@Test
	fun startup() =
		rule.collect(packageName = TARGET_PACKAGE, includeInStartupProfile = true) {
			pressHome()
			startActivityAndWait()
			waitForHome()
		}

	@Test
	fun browse() =
		rule.collect(packageName = TARGET_PACKAGE) {
			pressHome()
			startActivityAndWait()
			waitForHome()
			scrollHome()
			openDetail()
			scrollDetail()
		}
}
