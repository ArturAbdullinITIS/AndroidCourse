package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.ValidationError
import ru.itis.practice.domain.ValidationResult
import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject


class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): ValidationResult {
        var emailError: ValidationError? = null
        var passwordError: ValidationError? = null

        when {
            email.isBlank() -> emailError = ValidationError.BLANK_EMAIL
            !email.isValidEmail() -> emailError = ValidationError.INVALID_EMAIL_FORMAT
        }

        when {
            password.isBlank() -> passwordError = ValidationError.BLANK_PASSWORD
            password.length < 6 -> passwordError = ValidationError.WEAK_PASSWORD
        }

        return if (emailError != null || passwordError != null) {
            ValidationResult.Errors(emailError, passwordError)
        } else {
            userRepository.registerUser(email, password)
            ValidationResult.SUCCESS
        }
    }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.+)(\\.)(.+)".toRegex()
    return matches(emailRegex)
}