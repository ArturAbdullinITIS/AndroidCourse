package ru.itis.practice.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.R
import ru.itis.practice.domain.ValidationResult
import ru.itis.practice.domain.usecase.LoginUserUseCase
import ru.itis.practice.util.ResourceProvider
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val errorParser: ru.itis.practice.util.ErrorParser
): ViewModel() {


    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun processCommand(command: LoginCommand) {
        when(command) {
            LoginCommand.LoginUser -> {
                _state.update { it.copy(isSuccess = false) }
                viewModelScope.launch {
                    val currentEmail = _state.value.email
                    val currentPassword = _state.value.password
                    val validationResult = loginUserUseCase(currentEmail, currentPassword)

                    when(validationResult) {
                        is ValidationResult.SUCCESS -> {
                            Log.d("AuthViewModel", "User authenticated successfully")
                            _state.update { it.copy(isSuccess = true, emailError = "", passwordError = "") }
                        }
                        is ValidationResult.Errors -> {
                            _state.update { state ->
                                state.copy(
                                    emailError = validationResult.emailError?.let {
                                        errorParser.getErrorMessage(it)
                                    } ?: "",
                                    passwordError = validationResult.passwordError?.let {
                                        errorParser.getErrorMessage(it)
                                    } ?: ""
                                )
                            }
                        }

                        is ValidationResult.DeletedAccount -> {
                            _state.update { state->
                                state.copy(
                                    deletedAccount = true,
                                    email = currentEmail
                                )
                            }
                        }
                    }
                }
            }
            is LoginCommand.InputEmail -> {
                _state.update { state->
                    state.copy(
                        email = command.email,
                        emailError = ""
                    )
                }
            }
            is LoginCommand.InputPassword -> {
                _state.update { state->
                    state.copy(
                        password = command.password,
                        passwordError = ""
                    )
                }
            }

            LoginCommand.TogglePasswordVisibility -> {
                _state.update { state ->
                    state.copy(
                        isPassVis = !state.isPassVis
                    )
                }
            }
        }
    }
}

sealed interface LoginCommand {
    data class InputEmail(val email: String): LoginCommand
    data class InputPassword(val password: String): LoginCommand
    data object LoginUser: LoginCommand
    data object TogglePasswordVisibility: LoginCommand

}



data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val isSuccess: Boolean = false,
    val isPassVis: Boolean = false,
    val deletedAccount: Boolean = false
)
