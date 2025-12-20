package ru.itis.practice.presentation.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.itis.practice.R
import ru.itis.practice.presentation.screen.register.CustomClickableText

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToRecovery: (String) -> Unit
) {
    LoginContent(onNavigateToRegister, onNavigateToMain, onNavigateToRecovery)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToRecovery: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToMain()
        }
    }
    LaunchedEffect(state.deletedAccount) {
        if (state.deletedAccount) {
            onNavigateToRecovery(state.email)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomLogInIcon()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.enter_your_credentials_to_log_in),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 40.dp),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    focusedElevation = 8.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.log_in_screen),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EmailAuthTextField(
                        value = state.email,
                        onValueChange = {
                            viewModel.processCommand(LoginCommand.InputEmail(it))
                        },
                        errorMessage = state.emailError,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordAuthTextField(
                        value = state.password,
                        onValueChange = {
                            viewModel.processCommand(LoginCommand.InputPassword(it))
                        },
                        errorMessage = state.passwordError,
                        onIconClick = {
                            viewModel.processCommand(LoginCommand.TogglePasswordVisibility)
                        },
                        isPassVis = state.isPassVis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(30.dp))

                    LoginButton(
                        onClick = {
                            viewModel.processCommand(LoginCommand.LoginUser)
                        }
                    )

                    Spacer(modifier = Modifier.height(200.dp))

                    CustomClickableText(
                        text = stringResource(R.string.don_t_have_an_account_sign_up),
                        onClick = onNavigateToRegister
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
