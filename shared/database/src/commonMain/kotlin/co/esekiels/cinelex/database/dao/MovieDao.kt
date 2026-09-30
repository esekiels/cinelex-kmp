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
	@Query("SELECT * FROM MovieEntity WHERE category = :category ORDER BY position")
	fun observeMovieByCategory(category: String): Flow<List<MovieEntity>>

	@Query("SELECT * FROM MovieEntity WHERE category = :category ORDER BY position")
	suspend fun fetchByCategory(category: String): List<MovieEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveMovies(movies: List<MovieEntity>)

	@Query("DELETE FROM MovieEntity WHERE category = :category")
	suspend fun clearByCategory(category: String)

	@Transaction
	suspend fun replaceCategory(
		category: String,
		movies: List<MovieEntity>,
	) {
		clearByCategory(category)
		saveMovies(movies)
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveDetails(details: MovieDetailsEntity)

	@Query("SELECT * FROM MovieDetailsEntity WHERE id = :id")
	suspend fun fetchDetails(id: Int): MovieDetailsEntity?

	@Query("SELECT * FROM MovieDetailsEntity WHERE id = :id")
	fun observeDetails(id: Int): Flow<MovieDetailsEntity?>
}
