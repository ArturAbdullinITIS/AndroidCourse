package ru.itis.practice.presentation.screen.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject


@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(RecoveryScreenState())
    val state = _state.asStateFlow()
    fun processCommand(command: RecoveryCommand) {
        when(command) {
            is RecoveryCommand.DeletePermanently -> {
                viewModelScope.launch {
                    userRepository.findDeletedByEmail(command.email)?.let {
                        userRepository.hardDeleteUser(it.id)
                    }
                    userRepository.setSessionActive(false)
                    _state.update { state ->
                        state.copy(
                            deleted = true
                        )
                    }
                }
            }
            is RecoveryCommand.Recover -> {
                viewModelScope.launch {
                    userRepository.findDeletedByEmail(command.email)?.let {
                        userRepository.restoreUser(it.id)
                        userRepository.setActiveUser(it.id)
                        userRepository.setSessionActive(true)
                        _state.update { state->
                            state.copy(
                                recovered = true
                            )
                        }
                    }
                }
            }
        }
    }
}


sealed interface RecoveryCommand {
    data class DeletePermanently(val email: String): RecoveryCommand
    data class  Recover(val email: String): RecoveryCommand
}

data class RecoveryScreenState(
    val recovered: Boolean = false,
    val deleted: Boolean = false,
)