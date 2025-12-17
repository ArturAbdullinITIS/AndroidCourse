package ru.itis.practice.domain

import androidx.annotation.StringRes
import ru.itis.practice.R

sealed class ValidationResult {
    object SUCCESS : ValidationResult()
    data class Errors(
        val emailError: ValidationError? = null,
        val passwordError: ValidationError? = null
    ) : ValidationResult()
}

enum class ValidationError {
    BLANK_EMAIL,
    BLANK_PASSWORD,
    INVALID_EMAIL_FORMAT,
    WEAK_PASSWORD,
    AUTH_ERROR
}