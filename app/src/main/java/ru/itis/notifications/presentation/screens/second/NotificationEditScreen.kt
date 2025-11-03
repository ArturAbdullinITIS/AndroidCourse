package ru.itis.notifications.presentation.screens.second

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.notifications.presentation.screens.first.ContentField
import ru.itis.notifications.presentation.screens.first.CreateNotificationButton
import ru.itis.notifications.presentation.screens.second.ErrorMessage
import ru.itis.notifications.presentation.screens.first.ExpandableSwitch
import ru.itis.notifications.presentation.screens.first.NotificationSettingsCommand
import ru.itis.notifications.presentation.screens.first.OpenAppSwitch
import ru.itis.notifications.presentation.screens.first.PriorityDropdown
import ru.itis.notifications.presentation.screens.first.ReplyActionSwitch
import ru.itis.notifications.presentation.screens.first.TitleField


@Composable
fun NotificationEditScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: NotificationEditViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()


    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                NotificationIDField(
                    value = state.id,
                    onValueChange = {
                        viewModel.processCommand(NotificationEditCommand.InputID(it))
                    }
                )
                if(state.errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ErrorMessage(
                        message = state.errorMessage,
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                NotificationContentField(
                    value = state.content,
                    onValueChange = {
                        viewModel.processCommand(NotificationEditCommand.InputContent(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                UpdateNotificationButton(
                    onClick = {
                        viewModel.processCommand(NotificationEditCommand.UpdateNotification)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ClearAllNotificationsButton(
                    onClick = {
                        viewModel.processCommand(NotificationEditCommand.ClearAllNotifications)
                    }
                )
            }
        }
    }
}