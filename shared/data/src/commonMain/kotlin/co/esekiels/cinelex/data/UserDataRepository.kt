/*
 * Cinelex
 * UserDataRepository
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
	@NativeCoroutines
	val userPreferences: Flow<UserPreferences>

	@NativeCoroutines
	suspend fun setLanguage(language: Language?)

	@NativeCoroutines
	suspend fun setUiTheme(uiTheme: UiTheme)
}
