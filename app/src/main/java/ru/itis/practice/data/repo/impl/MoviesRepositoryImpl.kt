package ru.itis.practice.data.repo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val userId = userRepository.getActiveUserId() ?: -1

    override fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies(userId).map { it.toEntities() }
            .flowOn(Dispatchers.Default)
    }

    override suspend fun addMovie(
        title: String,
        description: String,
        year: Int,
        rating: Double
    ) {
        val userId = userRepository.getActiveUserId()
        val movieDbModel = MovieDbModel(
            userId = userId,
            title = title,
            description = description,
            releaseYear = year,
            rating = rating
        )
        movieDao.addMovie(movieDbModel)
    }



    override suspend fun deleteMovie(movieId: Int?) {
        movieDao.deleteMovie(userId, movieId)
    }
}