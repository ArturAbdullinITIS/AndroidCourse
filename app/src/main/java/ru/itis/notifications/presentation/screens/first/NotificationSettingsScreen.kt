package ru.itis.notifications.presentation.screens.first

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.notifications.domain.entities.Notification

@Composable
fun NotificationSettingsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: NotificationSettingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
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
                TitleField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = {
                        viewModel.processCommand(NotificationSettingsCommand.InputTitle(it))
                    }
                )
                if(state.errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ErrorMessage(
                        message = state.errorMessage,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                ContentField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.content,
                    onValueChange = {
                        viewModel.processCommand(NotificationSettingsCommand.InputContent(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PriorityDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    selectedPriority = state.priority,
                    onPrioritySelected = {
                        viewModel.processCommand(NotificationSettingsCommand.ChangePriority(it))
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExpandableSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    isExpandable = state.isExpandable,
                    onExpandableChanged = {
                        viewModel.processCommand(NotificationSettingsCommand.ExpandableSwitch(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OpenAppSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    shouldOpenApp = state.shouldOpenApp,
                    onOpenAppChanged = {
                        viewModel.processCommand(NotificationSettingsCommand.ShouldOpenAppSwitch(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReplyActionSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    hasReplyAction = state.hasReplyAction,
                    onReplyActionChanged = {
                        viewModel.processCommand(NotificationSettingsCommand.HasReplyActionSwitch(it))
                    }
                )



            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                CreateNotificationButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.processCommand(NotificationSettingsCommand.CreateNotification)
                    }
                )
            }
        }

    }
}