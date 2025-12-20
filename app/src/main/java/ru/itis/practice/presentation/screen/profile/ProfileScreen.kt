package ru.itis.practice.presentation.screen.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ru.itis.practice.R

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogIn: () -> Unit,
) {
    ProfileContent(onNavigateBack, onNavigateToLogIn)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    onNavigateBack: () -> Unit,
    onNavigateToLogIn: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.processCommand(ProfileCommand.AddImage(uri))

            }
        }
    )

    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(
                context,
                context.getString(R.string.username_updated_successfully),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(state.loggedOut) {
        if (state.loggedOut) {
            onNavigateToLogIn()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.large
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.profile),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if(state.image.isNotBlank()) {
                AsyncImage(
                    model = state.image,
                    contentDescription = "Profile picture",
                    Modifier.size(180.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                CustomProfileIcon()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PfpAddButton(
                    onClick = {
                        imagePicker.launch("image/*")
                    }
                )
                PfpDeleteButton(
                    onClick = {
                        viewModel.processCommand(ProfileCommand.DeleteImage)
                    }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            EmailProfileTextField(value = state.email)

            Spacer(modifier = Modifier.height(20.dp))

            UsernameEditRow(
                username = state.username,
                onUsernameChange = { viewModel.processCommand(ProfileCommand.InputUsername(it)) },
                onSaveUsername = { viewModel.processCommand(ProfileCommand.SetUsername) },
                errorMessage = state.errorMessage,
                isSuccess = state.success
            )

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            LogoutButton(
                onClick = { viewModel.processCommand(ProfileCommand.Logout) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            DeleteButton(
                onClick = { viewModel.processCommand(ProfileCommand.ShowDeleteDialog) }
            )
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.processCommand(ProfileCommand.CancelDelete) },
            title = { Text(stringResource(R.string.delete_account)) },
            text = {
                Text(
                    stringResource(
                        R.string.are_you_sure_you_want_to_delete_your_account_you_will_have_7_days_to_restore_it
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.processCommand(ProfileCommand.DeleteAccount) }
                ) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.processCommand(ProfileCommand.CancelDelete) }
                ) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

