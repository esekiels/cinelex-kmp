/*
 * Cinelex
 * CinelexMain
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import co.esekiels.cinelex.core.design.components.LanguageMenu
import co.esekiels.cinelex.core.design.components.ThemeMenu
import co.esekiels.cinelex.core.design.components.ZoomKey
import co.esekiels.cinelex.core.design.components.ZoomTransitionLayout
import co.esekiels.cinelex.core.design.components.zoomBounds
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.core.navigation.Detail
import co.esekiels.cinelex.core.navigation.Home
import co.esekiels.cinelex.core.navigation.Search
import co.esekiels.cinelex.feature.detail.DetailScreen
import co.esekiels.cinelex.feature.home.HomeScreen
import co.esekiels.cinelex.feature.search.SearchScreen
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences

@Composable
fun CinelexMain(
	preferences: UserPreferences,
	onLanguageSelected: (Language) -> Unit,
	onThemeSelected: (UiTheme) -> Unit,
) {
	CinelexTheme {
		ZoomTransitionLayout {
			val backStack = rememberNavBackStack(Home)
			val pop = { if (backStack.size > 1) backStack.removeLastOrNull() }
			val push = { key: NavKey -> if (backStack.last() != key) backStack.add(key) }
			// Home stays at the root so switching tabs never reloads it; back from Search returns Home.
			val selectTab = { tab: NavKey ->
				while (backStack.size > 1) backStack.removeLastOrNull()
				if (tab != Home) backStack.add(tab)
			}
			Scaffold(
				contentWindowInsets = WindowInsets(0),
				bottomBar = { if (backStack.last() in TABS) TabBar(backStack.last(), selectTab) },
			) { padding ->
				NavDisplay(
					modifier = Modifier.padding(padding).consumeWindowInsets(padding).semantics { testTagsAsResourceId = true },
					backStack = backStack,
					onBack = { pop() },
					entryDecorators =
						listOf(
							rememberSaveableStateHolderNavEntryDecorator(),
							rememberViewModelStoreNavEntryDecorator(),
						),
					entryProvider =
						entryProvider {
							entry<Home> {
								HomeScreen(onMovieClick = { movie, source -> push(Detail(movie.id, movie.title, source)) }) {
									LanguageMenu(preferences.language, onLanguageSelected)
									ThemeMenu(preferences.uiTheme, onThemeSelected)
								}
							}
							entry<Search> {
								SearchScreen(onMovieClick = { movie, source -> push(Detail(movie.id, movie.title, source)) })
							}
							entry<Detail> {
								Box(Modifier.zoomBounds(ZoomKey(it.movieId, it.source))) {
									DetailScreen(it.movieId, it.title, onBack = { pop() })
								}
							}
						},
				)
			}
		}
	}
}

private val TABS = listOf(Home, Search)

@Composable
private fun TabBar(
	selected: NavKey,
	onSelect: (NavKey) -> Unit,
) {
	NavigationBar {
		NavigationBarItem(
			selected = selected == Home,
			onClick = { onSelect(Home) },
			icon = { Icon(Icons.Default.Home, contentDescription = null) },
			label = { Text(stringResource(R.string.home)) },
		)
		NavigationBarItem(
			selected = selected == Search,
			onClick = { onSelect(Search) },
			icon = { Icon(Icons.Default.Search, contentDescription = null) },
			label = { Text(stringResource(R.string.search)) },
		)
	}
}
