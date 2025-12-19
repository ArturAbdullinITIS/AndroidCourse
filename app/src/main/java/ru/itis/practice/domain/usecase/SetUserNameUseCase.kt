package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject


data class UserNameValidationResult(
    val isValid: Boolean,
    val error: String
)
class SetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userName: String): UserNameValidationResult {
        var error = ""

        if(userName.isBlank()) {
            error = "User name is required"
        } else if(userName.length < 3) {
            error = "User name must be at least 3 characters"
        } else if(userName.length > 30) {
            error = "User name too long (max 30 chars)"
        }
        return if(error.isBlank()) {
            userRepository.setUserName(userName)
            UserNameValidationResult(true, "")
        } else {
            UserNameValidationResult(false, error)
        }
    }

}

