package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.MoviesRepository
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
){
    suspend operator fun invoke() {
        moviesRepository.deleteMovie()
    }
}