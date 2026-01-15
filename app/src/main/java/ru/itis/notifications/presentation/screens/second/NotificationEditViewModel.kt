package ru.itis.notifications.presentation.screens.second

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.notifications.R
import ru.itis.notifications.domain.usecases.ClearAllNotificationsUseCase
import ru.itis.notifications.domain.usecases.GetNotificationsUseCase
import ru.itis.notifications.domain.usecases.NotificationExistsUseCase
import ru.itis.notifications.domain.usecases.UpdateNotificationUseCase
import javax.inject.Inject


@HiltViewModel
class NotificationEditViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val notificationExistsUseCase: NotificationExistsUseCase,
    private val clearAllNotificationsUseCase: ClearAllNotificationsUseCase

): ViewModel() {
    private val _state = MutableStateFlow(NotificationEditState())
    val state  = _state.asStateFlow()

    fun processCommand(command: NotificationEditCommand) {

        when(command) {
            NotificationEditCommand.ClearAllNotifications -> {
                viewModelScope.launch {
                    val currentNotifications = getNotificationsUseCase().first()
                    if (currentNotifications.toList().isNotEmpty()) {
                        clearAllNotificationsUseCase()
                        Log.d("NotificationClear", "${currentNotifications.toList().size}")
                    } else {
                        Log.d("NotificationClear", "${currentNotifications.toList().size}")

                        Toast.makeText(context, context.getString(R.string.toast_no_notifications), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is NotificationEditCommand.InputContent -> {
                _state.update { prState ->
                    prState.copy(
                        content = command.content
                    )
                }
            }
            is NotificationEditCommand.InputID -> {
                _state.update { prState ->
                    prState.copy(
                        id = command.id,
                        errorMessage = ""
                    )
                }
            }
            NotificationEditCommand.UpdateNotification -> {
                val currentState = _state.value
                if(currentState.id.isBlank()) {
                    _state.update { prState ->
                        prState.copy(
                            errorMessage = context.getString(R.string.error_id_empty)
                        )
                    }
                } else {
                    val notificationId = currentState.id.toInt()
                    viewModelScope.launch{
                        val notificationExists = notificationExistsUseCase(notificationId)
                        if(notificationExists) {
                            updateNotificationUseCase(_state.value.id.toInt(), _state.value.content)
                        } else {
                            Toast.makeText(context, context.getString(R.string.toast_notification_not_exists), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }
}





sealed interface NotificationEditCommand {
    data class InputID(val id: String): NotificationEditCommand
    data class InputContent(val content: String): NotificationEditCommand
    data object UpdateNotification: NotificationEditCommand
    data object ClearAllNotifications: NotificationEditCommand
}
data class NotificationEditState(
    val id: String = "",
    val content: String = "",
    val errorMessage: String = ""
)
