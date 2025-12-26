package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.ValidationError
import ru.itis.practice.domain.ValidationResult
import ru.itis.practice.domain.repository.UserRepository
import ru.itis.practice.util.PasswordHasher
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(email: String, password: String): ValidationResult {
        val activeUser = userRepository.findUserByEmail(email)

        if (activeUser == null) {
            val deletedUser = userRepository.findDeletedByEmail(email)
            if (deletedUser != null) {
                return ValidationResult.DeletedAccount(deletedUser.email)
            }
            return ValidationResult.Errors(emailError = ValidationError.USER_NOT_FOUND)
        }

        if (!PasswordHasher.verify(password, activeUser.passwordHash)) {
            return ValidationResult.Errors(passwordError = ValidationError.WRONG_PASSWORD)
        }

        userRepository.setActiveUser(activeUser.id)
        return ValidationResult.SUCCESS
    }

}