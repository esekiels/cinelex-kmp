/*
 * Cinelex
 * UserPreferences
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.model

data class UserPreferences(
	val language: Language? = null,
	val uiTheme: UiTheme = UiTheme.FOLLOW_SYSTEM,
)

enum class Language(
	val code: String,
) {
	ENGLISH("en"),
	INDONESIAN("id"),
	;

	companion object {
		fun fromCode(code: String?): Language? = entries.firstOrNull { it.code == code }
	}
}

enum class UiTheme {
	FOLLOW_SYSTEM,
	LIGHT,
	DARK,
}
