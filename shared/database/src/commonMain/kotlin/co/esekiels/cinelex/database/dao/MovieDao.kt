/*
 * Cinelex
 * MovieDao
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.esekiels.cinelex.database.entity.MovieDetailsEntity
import co.esekiels.cinelex.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
	@Query("SELECT * FROM MovieEntity WHERE category = :category AND language = :language ORDER BY position")
	fun observeMovieByCategory(
		category: String,
		language: String,
	): Flow<List<MovieEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveMovies(movies: List<MovieEntity>)

	@Query("SELECT COUNT(*) FROM MovieEntity WHERE language = :language")
	suspend fun countMovies(language: String): Int

	@Query("DELETE FROM MovieEntity WHERE category = :category AND language = :language")
	suspend fun clearByCategory(
		category: String,
		language: String,
	)

	@Transaction
	suspend fun replaceCategory(
		category: String,
		language: String,
		movies: List<MovieEntity>,
	) {
		clearByCategory(category, language)
		saveMovies(movies)
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveDetails(details: MovieDetailsEntity)

	@Query("SELECT * FROM MovieDetailsEntity WHERE id = :id AND language = :language")
	fun observeDetails(
		id: Int,
		language: String,
	): Flow<MovieDetailsEntity?>
}
