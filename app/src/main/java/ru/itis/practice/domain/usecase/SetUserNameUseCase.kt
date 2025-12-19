package ru.itis.practice.domain.usecase

import ru.itis.practice.R
import ru.itis.practice.domain.repository.UserRepository
import ru.itis.practice.util.ResourceProvider
import javax.inject.Inject


data class UserNameValidationResult(
    val isValid: Boolean,
    val error: String
)
class SetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) {
    suspend operator fun invoke(userName: String): UserNameValidationResult {
        var error = ""

        if(userName.isBlank()) {
            error = resourceProvider.getString(R.string.user_name_is_required)
        } else if(userName.length < 3) {
            error = resourceProvider.getString(R.string.user_name_must_be_at_least_3_characters)
        } else if(userName.length > 30) {
            error = resourceProvider.getString(R.string.user_name_too_long_max_30_chars)
        }
        return if(error.isBlank()) {
            userRepository.setUserName(userName)
            UserNameValidationResult(true, "")
        } else {
            UserNameValidationResult(false, error)
        }
    }

}

