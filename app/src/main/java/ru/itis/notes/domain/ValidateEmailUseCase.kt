package ru.itis.notes.domain

import android.content.Context
import ru.itis.notes.presentation.utils.RegEx

class ValidateEmailUseCase {


    operator fun invoke(email: String): ValidationResult {
        val result: ValidationResult

        if(email.isBlank()) {
            result = ValidationResult.BLANK_EMAIL
        } else if(!email.matches(RegEx.EMAIL_REGEX)) {
            result = ValidationResult.INCORRECT_EMAIL_FORMAT
        } else {
            result = ValidationResult.SUCCESS
        }
        return result
    }
}
