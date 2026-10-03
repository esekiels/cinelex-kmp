/*
 * Cinelex
 * Common
 *
 * Created by Esekiel Surbakti on 03/10/26
 */

package co.esekiels.cinelex.benchmark

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

// Mirrors TestTag in androidApp; this module can't depend on the app.
const val HOME_LIST = "HomeList"
const val MOVIE_CARD = "MovieCard"
const val DETAIL_CONTENT = "DetailContent"

private const val LOAD_TIMEOUT_MS = 10_000L

const val TARGET_PACKAGE = "co.esekiels.cinelex"

fun MacrobenchmarkScope.waitForHome() {
	check(device.wait(Until.hasObject(By.res(MOVIE_CARD)), LOAD_TIMEOUT_MS)) { "Home never loaded" }
}

fun MacrobenchmarkScope.scrollHome() {
	val margin = device.displayWidth / 5

	device.findObject(By.hasChild(By.res(MOVIE_CARD))).run {
		setGestureMargin(margin)
		fling(Direction.RIGHT)
	}
	device.waitForIdle()

	val list = device.findObject(By.res(HOME_LIST))
	list.setGestureMargin(margin)
	list.fling(Direction.DOWN)
	device.waitForIdle()
	list.fling(Direction.UP)
	device.waitForIdle()
}

/** Opens the first movie and waits for its details, not just the skeleton. */
fun MacrobenchmarkScope.openDetail() {
	device.findObject(By.res(MOVIE_CARD)).click()
	check(device.wait(Until.hasObject(By.res(DETAIL_CONTENT)), LOAD_TIMEOUT_MS)) { "Detail never loaded" }
}

fun MacrobenchmarkScope.scrollDetail() {
	val content = device.findObject(By.res(DETAIL_CONTENT))
	content.setGestureMargin(device.displayWidth / 5)
	content.fling(Direction.DOWN)
	device.waitForIdle()
	content.fling(Direction.UP)
	device.waitForIdle()
}
