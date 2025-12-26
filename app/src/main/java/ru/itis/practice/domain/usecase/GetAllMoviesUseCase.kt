package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.MoviesRepository
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
){
    operator fun invoke() = moviesRepository.getAllMovies()
}