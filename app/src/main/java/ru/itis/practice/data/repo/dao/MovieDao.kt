package ru.itis.practice.data.repo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.practice.data.model.MovieDbModel


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE user_id = :userId")
    fun getAllMovies(userId: Int?): Flow<List<MovieDbModel>>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMovie(movieDbModel: MovieDbModel)

    @Query("DELETE FROM movies WHERE user_id = :userId")
    suspend fun deleteMovie(userId: Int?)
}