/*
 * Cinelex
 * appModules
 *
 * Created by Esekiel Surbakti on 26/09/26
 */

package co.esekiels.cinelex.core.di

import co.esekiels.cinelex.MainViewModel
import co.esekiels.cinelex.feature.detail.DetailViewModel
import co.esekiels.cinelex.feature.home.HomeViewModel
import co.esekiels.cinelex.feature.search.SearchViewModel
import co.esekiels.cinelex.feature.watchlist.WatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
	module {
		viewModelOf(::MainViewModel)
		viewModelOf(::HomeViewModel)
		viewModelOf(::SearchViewModel)
		viewModelOf(::WatchlistViewModel)
		viewModel { (movieId: Int) -> DetailViewModel(get(), get(), movieId) }
	}
