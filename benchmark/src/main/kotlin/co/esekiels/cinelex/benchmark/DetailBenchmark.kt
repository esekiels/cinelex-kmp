/*
 * Cinelex
 * DetailBenchmark
 *
 * Created by Esekiel Surbakti on 03/10/26
 */

package co.esekiels.cinelex.benchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailBenchmark {
	@get:Rule
	val rule = MacrobenchmarkRule()

	@Test
	fun openFromHome() = benchmark(setup = { launchHome() }) { openDetail() }

	@Test
	fun scrollContent() =
		benchmark(setup = {
			launchHome()
			openDetail()
		}) { scrollDetail() }

	private fun benchmark(
		setup: MacrobenchmarkScope.() -> Unit,
		measure: MacrobenchmarkScope.() -> Unit,
	) = rule.measureRepeated(
		packageName = TARGET_PACKAGE,
		metrics = listOf(FrameTimingMetric()),
		compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
		startupMode = StartupMode.WARM,
		iterations = 5,
		setupBlock = setup,
		measureBlock = measure,
	)

	private fun MacrobenchmarkScope.launchHome() {
		pressHome()
		startActivityAndWait()
		waitForHome()
	}
}
