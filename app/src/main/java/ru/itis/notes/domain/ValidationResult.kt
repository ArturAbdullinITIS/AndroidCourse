package ru.itis.notes.domain
import ru.itis.practice.R
import android.content.Context
import androidx.annotation.StringRes

enum class ValidationResult(@StringRes val resId: Int) {
    SUCCESS(R.string.validation_success),
    BLANK_EMAIL(R.string.email_blank_error),
    INCORRECT_EMAIL_FORMAT(R.string.email_format_error),
    BLANK_PASSWORD(R.string.password_blank_error),
    SHORT_PASSWORD(R.string.password_short_error)
}