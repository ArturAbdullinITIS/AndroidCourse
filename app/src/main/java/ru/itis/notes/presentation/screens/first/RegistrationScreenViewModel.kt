package ru.itis.notes.presentation.screens.first

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notes.domain.ValidateEmailUseCase
import ru.itis.notes.domain.ValidatePasswordUseCase
import ru.itis.notes.domain.ValidateRegistrationFormUseCase

class RegistrationScreenViewModel {
    private val validateRegistrationFormUseCase = ValidateRegistrationFormUseCase()
    private val validatePasswordUseCase = ValidatePasswordUseCase()
    private val validateEmailUseCase = ValidateEmailUseCase()

    private val _state = MutableStateFlow(RegistrationScreenState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegistrationScreenCommand) {
        when(command) {
            is RegistrationScreenCommand.InputEmail -> {
                _state.update { state ->
                    state.copy(
                        email = command.email,
                        emailError = "",
                        errorMessage = ""
                    )
                }
            }
            is RegistrationScreenCommand.InputPassword -> {
                _state.update { state ->
                    state.copy(
                        password = command.password,
                        passwordError = "",
                        errorMessage = ""
                    )
                }
            }
            RegistrationScreenCommand.Register -> {
                _state.update { currentState ->
                    val email = currentState.email
                    val password = currentState.password

                    val emailValidationResult = validateEmailUseCase(email)
                    val emailError = emailValidationResult != "Success"

                    val passwordValidationResult = validatePasswordUseCase(password)
                    val passwordError = passwordValidationResult != "Success"
                    if (emailError || passwordError) {
                        currentState.copy(
                            success = false,
                            emailError = if (emailError) emailValidationResult else "",
                            passwordError = if (passwordError) passwordValidationResult else "",
                            errorMessage = if (emailError) emailValidationResult else passwordValidationResult
                        )
                    } else if (validateRegistrationFormUseCase(email, password)) {
                        currentState.copy(
                            success = true,
                            emailError = "",
                            passwordError = "",
                            errorMessage = ""
                        )
                    } else {
                        currentState.copy(
                            success = false,
                            errorMessage = "Form validation failed"
                        )
                    }
                }
            }
            RegistrationScreenCommand.ChangeVisibility -> {
                _state.update { state ->
                    state.copy(isPasswordVisible = !state.isPasswordVisible)
                }
            }
        }
    }
}

sealed interface RegistrationScreenCommand {
    data class InputEmail(val email: String): RegistrationScreenCommand
    data class InputPassword(val password: String): RegistrationScreenCommand
    data object Register: RegistrationScreenCommand
    data object ChangeVisibility: RegistrationScreenCommand
}

data class RegistrationScreenState(
    val email: String = "",
    val password: String = "",
    val success: Boolean = false,
    val errorMessage: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val isPasswordVisible: Boolean = false
)