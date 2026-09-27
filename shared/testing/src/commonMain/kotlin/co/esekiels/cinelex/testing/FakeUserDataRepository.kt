/*
 * Cinelex
 * FakeUserDataRepository
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.testing

import co.esekiels.cinelex.data.UserDataRepository
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserDataRepository(
	initial: UserPreferences = UserPreferences(),
) : UserDataRepository {
	constructor() : this(UserPreferences())

	override val userPreferences = MutableStateFlow(initial)

	override suspend fun setLanguage(language: Language?) {
		userPreferences.value = userPreferences.value.copy(language = language)
	}

	override suspend fun setUiTheme(uiTheme: UiTheme) {
		userPreferences.value = userPreferences.value.copy(uiTheme = uiTheme)
	}
}
