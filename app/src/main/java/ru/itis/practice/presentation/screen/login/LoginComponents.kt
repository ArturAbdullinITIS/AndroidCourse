package ru.itis.practice.presentation.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
        shape = RoundedCornerShape(40.dp),
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
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Mail,
                contentDescription = stringResource(R.string.email_auth_field)
            )
        }
    )
}

@Composable
fun PasswordAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    isPassVis: Boolean,
    onIconClick: () -> Unit
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
        shape = RoundedCornerShape(40.dp),
        singleLine = true,
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = onIconClick
                    ),
                imageVector = Icons.Default.RemoveRedEye,
                contentDescription = stringResource(R.string.password_auth_field)
            )
        },
        isError = errorMessage.isNotBlank(),
        visualTransformation = if(!isPassVis) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(40.dp)
    ) {
        Text(text = stringResource(R.string.log_in))
    }
}

@Composable
fun CustomLogInIcon() {
    Icon(
        modifier = Modifier.size(100.dp),
        imageVector = Icons.Default.Key,
        contentDescription = stringResource(R.string.log_in_icon),
        tint = MaterialTheme.colorScheme.onPrimary,
    )
}