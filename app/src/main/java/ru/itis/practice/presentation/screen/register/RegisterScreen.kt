package ru.itis.practice.presentation.screen.register

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RegisterScreen(
) {
    RegisterContent()
}


@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text(
                    text = "Registration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                EmailTextField(
                    value = state.email,
                    onValueChange = {
                        viewModel.processCommand(RegisterCommand.InputEmail(it))
                    },
                    errorMessage = state.emailError,
                )
                Spacer(modifier = Modifier.height(4.dp))
                PasswordTextField(
                    value = state.password,
                    onValueChange = {
                        viewModel.processCommand(RegisterCommand.InputPassword(it))
                    },
                    errorMessage = state.passwordError,
                )
                Spacer(modifier = Modifier.height(8.dp))

            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                RegisterButton(
                    onClick = {
                        viewModel.processCommand(RegisterCommand.RegisterUser)
                    }
                )
            }
        }
    }
}