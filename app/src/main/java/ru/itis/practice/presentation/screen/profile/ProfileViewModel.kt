package ru.itis.practice.presentation.screen.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.domain.usecase.DeleteUserUseCase
import ru.itis.practice.domain.usecase.GetUserEmailUseCase
import ru.itis.practice.domain.usecase.GetUsernameUseCase
import ru.itis.practice.domain.usecase.LogoutUseCase
import ru.itis.practice.domain.usecase.SetUserNameUseCase
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setUserNameUseCase: SetUserNameUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val deleteAccountUseCase: DeleteUserUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state.asStateFlow()
    private var oldName: String? = null
    init {
        viewModelScope.launch {
            val userEmail = getUserEmailUseCase()
            val username = getUsernameUseCase()
            oldName = username
            _state.update { state ->
                state.copy(
                    email = userEmail,
                    username = username
                )
            }
        }
    }

    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.InputUsername -> {
                _state.update { state ->
                    state.copy(
                        username = command.username
                    )
                }
            }
            is ProfileCommand.Logout -> {
                viewModelScope.launch {
                    logoutUseCase()
                }
                _state.update { state ->
                    state.copy(
                        loggedOut = true
                    )
                }
            }

            ProfileCommand.SetUsername -> {
                viewModelScope.launch {
                    val username = state.value.username
                    val result = setUserNameUseCase(username)
                    if (username != oldName && result.isValid) {
                        oldName = username
                        _state.update { state ->
                            state.copy(
                                success = true,
                                errorMessage = ""
                            )
                        }
                    } else {
                        _state.update { state ->
                            state.copy(
                                errorMessage = result.error
                            )
                        }
                    }
                }
            }

            ProfileCommand.DeleteAccount -> {
                viewModelScope.launch {
                    deleteAccountUseCase()
                    _state.update { state->
                        state.copy(
                            loggedOut = true,
                            showDeleteDialog = false
                        )
                    }
                }
            }

            ProfileCommand.ShowDeleteDialog -> {
                _state.update { state ->
                    state.copy(showDeleteDialog = true)
                }
            }

            ProfileCommand.CancelDelete -> {
                _state.update { state ->
                    state.copy(showDeleteDialog = false)
                }
            }
        }
    }
}

sealed interface ProfileCommand {
    data class InputUsername(val username: String) : ProfileCommand
    data object Logout : ProfileCommand
    data object SetUsername : ProfileCommand
    data object DeleteAccount : ProfileCommand
    data object ShowDeleteDialog : ProfileCommand
    data object CancelDelete : ProfileCommand
}

data class ProfileScreenState(
    val username: String = "",
    val email: String = "",
    val errorMessage: String = "",
    val success: Boolean = false,
    val loggedOut: Boolean = false,
    val showDeleteDialog: Boolean = false
)