package ru.itis.notes.domain

class ValidateRegistrationFormUseCase() {
    private val validateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase = ValidatePasswordUseCase()


    operator fun invoke(email: String, password: String): Boolean {
        return validateEmailUseCase(email) == ValidationResult.SUCCESS &&
                validatePasswordUseCase(password) == ValidationResult.SUCCESS
    }
}