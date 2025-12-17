package ru.itis.practice.presentation.screen.register

import android.R.attr.title
import android.R.id.title
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.practice.R

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    RegisterContent(onNavigateToLogin)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterContent(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToLogin()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
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
                    CustomSignUpIcon()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Sign Up",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EmailTextField(
                        value = state.email,
                        onValueChange = {
                            viewModel.processCommand(RegisterCommand.InputEmail(it))
                        },
                        errorMessage = state.emailError,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordTextField(
                        value = state.password,
                        onValueChange = {
                            viewModel.processCommand(RegisterCommand.InputPassword(it))
                        },
                        errorMessage = state.passwordError,
                        onIconClick = {
                            viewModel.processCommand(RegisterCommand.TogglePasswordVisibility)
                        },
                        isPasswordVisible = state.isPassVis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(20.dp))

                    RegisterButton(
                        onClick = {
                            viewModel.processCommand(RegisterCommand.RegisterUser)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CustomClickableText(
                        text = "Already have an account? Log In",
                        onClick = onNavigateToLogin
                    )
                    Spacer(modifier = Modifier.height(50.dp))

                }
            }
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
