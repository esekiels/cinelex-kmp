/*
 * Cinelex
 * HomeScrollBenchmark
 *
 * Created by Esekiel Surbakti on 03/10/26
 */

package co.esekiels.cinelex.benchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScrollBenchmark {
	@get:Rule
	val rule = MacrobenchmarkRule()

	@Test
	fun scrollHome() =
		rule.measureRepeated(
			packageName = TARGET_PACKAGE,
			metrics = listOf(FrameTimingMetric()),
			compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
			startupMode = StartupMode.WARM,
			iterations = 5,
			setupBlock = {
				pressHome()
				startActivityAndWait()
				waitForHome()
			},
		) {
			scrollHome()
		}
}
