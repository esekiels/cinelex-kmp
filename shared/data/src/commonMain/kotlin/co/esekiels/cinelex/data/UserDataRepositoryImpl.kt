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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

internal class UserDataRepositoryImpl(
	private val dataSource: UserPreferencesDataSource,
	private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserDataRepository {
	override val userPreferences: Flow<UserPreferences> = dataSource.data.catch { throw it.toCinelexException() }

	override suspend fun setLanguage(language: Language?) = ioDispatcher.guarded { dataSource.setLanguage(language) }

	override suspend fun setUiTheme(uiTheme: UiTheme) = ioDispatcher.guarded { dataSource.setUiTheme(uiTheme) }
}
