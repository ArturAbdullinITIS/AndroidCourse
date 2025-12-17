package ru.itis.practice.presentation.screen.register

import android.R.attr.contentDescription
import android.widget.Button
import androidx.compose.animation.EnterTransition.Companion.None
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.itis.practice.R


@Composable
fun EmailTextField(
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
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Mail,
                contentDescription = "Email"
            )
        }
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    onIconClick: () -> Unit,
    isPasswordVisible: Boolean
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
        visualTransformation = if (!isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        isError = errorMessage.isNotBlank(),
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = onIconClick
                    ),
                imageVector = Icons.Default.RemoveRedEye,
                contentDescription = "Password"
            )
        },
    )
}

@Composable
fun CustomClickableText(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        textAlign = TextAlign.Center,
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun RegisterButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.size(width = 300.dp, height = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            "Sign Up",
        )
    }
}

@Composable
fun CustomSignUpIcon() {
    Icon(
        modifier = Modifier.size(100.dp),
        imageVector = Icons.Default.AccountCircle,
        contentDescription = "Sign Up Icon",
        tint = MaterialTheme.colorScheme.primary,
    )
}
