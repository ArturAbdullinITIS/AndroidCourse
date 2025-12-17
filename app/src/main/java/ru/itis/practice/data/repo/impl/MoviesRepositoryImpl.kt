package ru.itis.practice.data.repo.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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


    override fun getAllMovies(): Flow<List<Movie>> = flow{
        val userId = userRepository.getActiveUserId()

        movieDao.getAllMovies(userId).map { it.toEntities() }
            .collect {
                emit(it)
            }

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