package ru.itis.practice.presentation.screen.register

import android.R.attr.contentDescription
import android.R.attr.singleLine
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
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
                contentDescription = stringResource(R.string.email_tr_icon)
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
                contentDescription = stringResource(R.string.password_tr_icon)
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
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun RegisterButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(40.dp)
    ) {
        Text(
            stringResource(R.string.sign_up),
        )
    }
}

@Composable
fun CustomSignUpIcon() {
    Icon(
        modifier = Modifier.size(100.dp),
        imageVector = Icons.Default.PersonAdd,
        contentDescription = stringResource(R.string.sign_up_icon_custom),
        tint = MaterialTheme.colorScheme.onPrimary,
    )
}
