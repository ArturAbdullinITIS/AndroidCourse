package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.deleteImage()
    }
}