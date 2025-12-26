package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject

class GetUserImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        return userRepository.getUserImage()
    }
}