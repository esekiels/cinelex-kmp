/*
 * Cinelex
 * MainViewModel
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
	private val repository: UserDataRepository,
) : ViewModel() {
	val preferences: StateFlow<UserPreferences> =
		repository.userPreferences.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
			initialValue = UserPreferences(),
		)

	fun setLanguage(language: Language?) {
		viewModelScope.launch { repository.setLanguage(language) }
	}

	fun setUiTheme(uiTheme: UiTheme) {
		viewModelScope.launch { repository.setUiTheme(uiTheme) }
	}

	fun syncLanguage(system: Language?) {
		viewModelScope.launch {
			if (repository.userPreferences.first().language != system) repository.setLanguage(system)
		}
	}
}

private const val STOP_TIMEOUT_MILLIS = 5_000L
