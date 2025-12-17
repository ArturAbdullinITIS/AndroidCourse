package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.ValidationError
import ru.itis.practice.domain.ValidationResult
import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(email: String, password: String): ValidationResult {
        val loginSuccessful = userRepository.loginUser(email, password)
        return if (loginSuccessful) {
            ValidationResult.SUCCESS
        } else {
            ValidationResult.Errors(
                emailError = ValidationError.AUTH_ERROR,
                passwordError = ValidationError.AUTH_ERROR
            )
        }
    }
}