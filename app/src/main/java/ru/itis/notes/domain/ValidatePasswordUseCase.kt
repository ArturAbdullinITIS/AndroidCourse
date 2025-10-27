package ru.itis.notes.domain

class ValidatePasswordUseCase {

    operator fun invoke(password: String): ValidationResult {
        val result: ValidationResult
        if(password.isBlank()) {
            result = ValidationResult.BLANK_PASSWORD
        } else if(password.length < 8) {
            result = ValidationResult.SHORT_PASSWORD
        } else {
            result = ValidationResult.SUCCESS
        }
        return result
    }

}