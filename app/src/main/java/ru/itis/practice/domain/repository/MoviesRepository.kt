package ru.itis.practice.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.practice.domain.entity.Movie

interface MoviesRepository {

    fun getAllMovies(): Flow<List<Movie>>

    suspend fun addMovie(
        title: String,
        description: String,
        year: Int,
        rating: Double
    )

}