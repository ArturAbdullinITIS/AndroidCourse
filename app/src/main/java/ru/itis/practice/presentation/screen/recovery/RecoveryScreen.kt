package ru.itis.practice.presentation.screen.recovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.practice.R


@Composable
fun RecoveryScreen(
    email: String,
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    RecoveryContent(email, onNavigateToMain, onNavigateToLogin)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryContent(
    email: String,
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.recovered) {
        if (state.recovered) onNavigateToMain()
    }
    LaunchedEffect(state.deleted) {
        if (state.deleted) onNavigateToLogin()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.account_recovery)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.PersonOff,
                stringResource(R.string.deleted_account),
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(R.string.account_deleted),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(R.string.your_account_was_deleted_but_can_be_restored),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    viewModel.processCommand(RecoveryCommand.Recover(email)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.restore_account))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.processCommand(RecoveryCommand.ShowDeleteDialog)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.delete_permanently))
            }
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.processCommand(RecoveryCommand.CancelDelete) },
            title = { Text(stringResource(R.string.delete_account_permanently)) },
            text = { Text(stringResource(R.string.this_action_cannot_be_undone_are_you_sure_you_want_to_permanently_delete_your_account)) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.processCommand(RecoveryCommand.DeletePermanently(email)) }
                ) { Text(stringResource(R.string.delete_alert_recovery)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.processCommand(RecoveryCommand.CancelDelete) }
                ) { Text(stringResource(R.string.cancel_alert_recovery)) }
            }
        )
    }
}