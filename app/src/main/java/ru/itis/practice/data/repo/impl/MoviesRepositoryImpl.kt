package ru.itis.practice.data.repo.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.itis.practice.data.mapper.toEntities
import ru.itis.practice.data.model.MovieDbModel
import ru.itis.practice.data.repo.dao.MovieDao
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.repository.MoviesRepository
import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val userRepository: UserRepository
): MoviesRepository {
    override fun getAllMovies(): Flow<List<Movie>> {
        val userId = userRepository.getActiveUserId()
        return movieDao.getAllMovies(userId).map { it.toEntities() }
    }
    override suspend fun addMovie(
        title: String,
        description: String,
        director: String,
        year: Int
    ) {
        val userId = userRepository.getActiveUserId()
        val movieDbModel = MovieDbModel(
            userId = userId,
            title = title,
            description = description,
            releaseYear = year
        )
        movieDao.addMovie(movieDbModel)
    }
}