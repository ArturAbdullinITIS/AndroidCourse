package ru.itis.practice.presentation.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.practice.R

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    LoginContent(onNavigateToRegister, onNavigateToMain)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToMain()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inversePrimary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    focusedElevation = 8.dp
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    CustomLogInIcon()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Log In",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
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

                    Spacer(modifier = Modifier.height(20.dp))

                    LoginButton(
                        onClick = {
                            viewModel.processCommand(LoginCommand.LoginUser)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier
                            .clickable { onNavigateToRegister() },
                        text = "Don't have an account? Sign Up",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
