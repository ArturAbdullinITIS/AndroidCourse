package ru.itis.notifications.presentation.screens.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.notifications.domain.entities.Message
import ru.itis.notifications.domain.usecases.AddMessageUseCase
import ru.itis.notifications.domain.usecases.GetMessagesUseCase
import javax.inject.Inject


@HiltViewModel
class UserMessagesViewModel @Inject constructor(
    private val addMessageUseCase: AddMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(UserMessagesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getMessagesUseCase().collect() { messages ->
                _state.update { prState ->
                    prState.copy(
                        messages = messages
                    )
                }
            }
        }
    }

    fun processCommand(command: UserMessagesCommand) {
        when(command) {
            is UserMessagesCommand.InputMessage -> {
                _state.update { prState ->
                    prState.copy(
                        message = command.message,
                        isSendable = command.message.isNotBlank()
                    )
                }
            }
            UserMessagesCommand.SendMessage -> {
                viewModelScope.launch {
                    val message = _state.value.message
                    addMessageUseCase(message, false)
                    _state.update { prState ->
                        prState.copy(
                            message = "",
                            isSendable = false
                        )
                    }
                }
            }
        }
    }
}

sealed interface UserMessagesCommand {

    data object SendMessage: UserMessagesCommand
    data class InputMessage(val message: String): UserMessagesCommand
}

data class UserMessagesState(
    val message: String = "",
    val messages: List<Message> = listOf(),
    val isSendable: Boolean = false
)