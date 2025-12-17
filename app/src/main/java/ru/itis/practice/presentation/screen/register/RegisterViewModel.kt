package ru.itis.practice.presentation.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.R
import ru.itis.practice.domain.ValidationError
import ru.itis.practice.domain.ValidationResult
import ru.itis.practice.domain.usecase.RegisterUserUseCase
import ru.itis.practice.util.ResourceProvider
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val resourceProvider: ResourceProvider
): ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegisterCommand) {
        when(command) {
            is RegisterCommand.InputEmail -> {
                _state.update { state ->
                    state.copy(
                        email = command.email,
                        emailError = ""
                    )
                }
            }
            is RegisterCommand.InputPassword -> {
                _state.update { state ->
                    state.copy(
                        password = command.password,
                        passwordError = ""
                    )
                }
            }
            is RegisterCommand.RegisterUser -> {
                viewModelScope.launch {
                    _state.update {it.copy(isSuccess = false)}
                    val currentEmail = _state.value.email
                    val currentPassword = _state.value.password

                    val validationResult = registerUserUseCase(currentEmail, currentPassword)

                    when(validationResult) {
                        is ValidationResult.SUCCESS -> {
                            _state.update { state ->
                                state.copy(
                                    isSuccess = true
                                )
                            }
                        }
                        is ValidationResult.Errors -> {
                            _state.update { state ->
                                state.copy(
                                    emailError = validationResult.emailError?.let {
                                        getErrorMessage(it)
                                    } ?: "",
                                    passwordError = validationResult.passwordError?.let {
                                        getErrorMessage(it)
                                    } ?: ""
                                )
                            }
                        }
                    }
                }
            }

            RegisterCommand.TogglePasswordVisibility -> {
                _state.update { state ->
                    state.copy(
                        isPassVis = !state.isPassVis
                    )
                }
            }
        }
    }

    private fun getErrorMessage(error: ValidationError): String {
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
        }
    }
}

sealed interface RegisterCommand {
    data class InputEmail(val email: String): RegisterCommand
    data class InputPassword(val password: String): RegisterCommand
    data object RegisterUser: RegisterCommand
    data object TogglePasswordVisibility: RegisterCommand
}

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val isSuccess: Boolean = false,
    val isPassVis: Boolean = false
)