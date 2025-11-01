package ru.itis.notifications.presentation.screens.first

import android.util.Log
import androidx.compose.runtime.produceState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.domain.usecases.CreateNotificationUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val createNotificationUseCase: CreateNotificationUseCase
): ViewModel() {
    private val _state = MutableStateFlow(
        NotificationSettingsState())

    val state = _state.asStateFlow()

    fun processCommand(command: NotificationSettingsCommand) {
        when(command) {
            is NotificationSettingsCommand.InputContent -> {
                _state.update { prState ->
                    prState.copy(
                        content = command.content
                    )
                }
            }
            is NotificationSettingsCommand.InputTitle -> {
                _state.update { prState ->
                    prState.copy(
                        title = command.title,
                        titleError = command.title.isBlank(),
                        errorMessage = ""
                    )
                }
            }

            is NotificationSettingsCommand.ChangePriority -> {
                _state.update { prState ->
                    prState.copy(
                        priority = command.priority
                    )
                }
            }
            is NotificationSettingsCommand.ExpandableSwitch -> {
                _state.update { it.copy(isExpandable = command.isExpandable) }
            }
            is NotificationSettingsCommand.ShouldOpenAppSwitch -> {
                _state.update { it.copy(shouldOpenApp = command.shouldOpenApp) }
            }
            is NotificationSettingsCommand.HasReplyActionSwitch -> {
                _state.update { it.copy(hasReplyAction = command.hasReplyAction) }
            }
            is NotificationSettingsCommand.CreateNotification -> {
                val currentState = _state.value
                if (!currentState.titleError) {
                    viewModelScope.launch {
                        createNotificationUseCase(
                            title = currentState.title,
                            content = currentState.content,
                            priority = currentState.priority,
                            isExpandable = currentState.isExpandable,
                            shouldOpenApp = currentState.shouldOpenApp,
                            hasReplyAction = currentState.hasReplyAction
                        )
                    }

                } else {
                    _state.update { prState ->
                        prState.copy(
                            errorMessage = "Title cannot be blank!"
                        )
                    }
                }
            }
        }
    }
}



sealed interface NotificationSettingsCommand {


    data class InputTitle(val title: String): NotificationSettingsCommand
    data class InputContent(val content: String): NotificationSettingsCommand
    data class ExpandableSwitch(val isExpandable: Boolean): NotificationSettingsCommand
    data class ChangePriority(val priority: NotificationPriority): NotificationSettingsCommand
    data class ShouldOpenAppSwitch(val shouldOpenApp: Boolean): NotificationSettingsCommand
    data class HasReplyActionSwitch(val hasReplyAction: Boolean): NotificationSettingsCommand
    object CreateNotification : NotificationSettingsCommand
}

data class NotificationSettingsState(
    val title: String = "",
    val content: String = "",
    val isExpandable: Boolean = false,
    val priority: NotificationPriority = NotificationPriority.MEDIUM,
    val shouldOpenApp: Boolean = false,
    val hasReplyAction: Boolean = false,
    val titleError: Boolean = true,
    val errorMessage: String = ""
)


