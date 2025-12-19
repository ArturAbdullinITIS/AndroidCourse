package ru.itis.practice.util

import dagger.hilt.android.qualifiers.ApplicationContext
import ru.itis.practice.R
import ru.itis.practice.domain.ValidationError
import javax.inject.Inject

open class ErrorParser  constructor(
    private val resourceProvider: ResourceProvider
) {
    fun getErrorMessage(
        error: ValidationError,
    ): String {
        return when(error) {
            ValidationError.BLANK_EMAIL ->
                resourceProvider.getString(R.string.email_cannot_be_blank)
            ValidationError.BLANK_PASSWORD ->
                resourceProvider.getString(R.string.password_cannot_be_blank)
            ValidationError.INVALID_EMAIL_FORMAT ->
                resourceProvider.getString(R.string.invalid_email_format)
            ValidationError.WEAK_PASSWORD ->
                resourceProvider.getString(R.string.password_is_too_weak)
            ValidationError.AUTH_ERROR ->
                resourceProvider.getString(R.string.authentication_error_occurred)
            ValidationError.EMAIL_ALREADY_EXISTS -> {
                resourceProvider.getString(R.string.email_already_exists)
            }
            ValidationError.USER_NOT_FOUND -> {
                resourceProvider.getString(R.string.no_user_with_this_email_was_found)
            }
            ValidationError.WRONG_PASSWORD -> {
                resourceProvider.getString(R.string.wrong_password_entered)
            }
        }
    }
}
