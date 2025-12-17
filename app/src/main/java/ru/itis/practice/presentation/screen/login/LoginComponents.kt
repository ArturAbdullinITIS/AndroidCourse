package ru.itis.practice.presentation.screen.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.itis.practice.R

@Composable
fun EmailAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(R.string.email))
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = errorMessage.isNotBlank(),
        placeholder = {
            Text(stringResource(R.string.type_your_email))
        }
    )
}

@Composable
fun PasswordAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(R.string.password))
        },
        placeholder = {
            Text(stringResource(R.string.type_your_password))
        },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = errorMessage.isNotBlank(),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = stringResource(R.string.login))
    }
}