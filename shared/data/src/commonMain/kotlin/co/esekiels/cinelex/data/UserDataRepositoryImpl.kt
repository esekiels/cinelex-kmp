/*
 * Cinelex
 * UserDataRepositoryImpl
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.data

import co.esekiels.cinelex.datastore.UserPreferencesDataSource
import co.esekiels.cinelex.model.Language
import co.esekiels.cinelex.model.UiTheme
import co.esekiels.cinelex.model.UserPreferences
import kotlinx.coroutines.flow.Flow

internal class UserDataRepositoryImpl(
	private val dataSource: UserPreferencesDataSource,
) : UserDataRepository {
	override val userPreferences: Flow<UserPreferences> = dataSource.data

	override suspend fun setLanguage(language: Language?) = dataSource.setLanguage(language)

	override suspend fun setUiTheme(uiTheme: UiTheme) = dataSource.setUiTheme(uiTheme)
}
