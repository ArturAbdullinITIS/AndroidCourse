package ru.itis.notifications.presentation.screens.third

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


@Composable
fun UserMessagesScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: UserMessagesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .weight(1f),

            ) {
                items(state.messages) { message ->
                    Message(
                        bgColor = MaterialTheme.colorScheme.primary,
                        message = message
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                MessageField(
                    modifier = Modifier.weight(1f),
                    value = state.message,
                    onValueChange = {
                        viewModel.processCommand(UserMessagesCommand.InputMessage(it))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SendMessageButton(
                    modifier = Modifier.weight(0.5f),
                    enabled = state.isSendable,
                    onClick = {
                        viewModel.processCommand(UserMessagesCommand.SendMessage)
                    }
                )
            }

        }
    }
}