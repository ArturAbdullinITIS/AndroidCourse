package ru.itis.practice.presentation.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.practice.R
import ru.itis.practice.presentation.ui.theme.Green


@Composable
fun CustomProfileIcon() {
    Icon(
        modifier = Modifier.size(180.dp),
        imageVector = Icons.Default.AccountCircle,
        contentDescription = stringResource(R.string.sign_up_icon),
        tint = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun EmailProfileTextField(
    value: String
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {},
        label = {
            Text(stringResource(R.string.your_email))
        },
        shape = RoundedCornerShape(40.dp),
        singleLine = true,
        enabled = false,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(R.string.username),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
fun UsernameTextField(
    success: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    onEditUsername: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.username_label)) },
        placeholder = { Text(stringResource(R.string.enter_your_username)) },
        shape = RoundedCornerShape(40.dp),
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = errorMessage.isNotBlank(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.username_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            if (success) {
                Text("Username changed!", color = Green)
            }
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit username",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                    onEditUsername()
                }
            )
        }
    )
}

@Composable
fun PfpAddButton(
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier,
        onClick = onClick,
        shape = RoundedCornerShape(40.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = stringResource(R.string.pfp)
        )
    }
}


@Composable
fun PfpDeleteButton(
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier,
        onClick = onClick,
        shape = RoundedCornerShape(40.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.65f),
            contentColor = MaterialTheme.colorScheme.onError
        )
    ) {
        Icon(
            imageVector = Icons.Default.HideImage,
            contentDescription = stringResource(R.string.pfp)
        )
    }
}


@Composable
fun LogoutButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.size(width = 300.dp, height = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(40.dp)
    ) {
        Text(
            stringResource(R.string.log_out),
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.size(width = 300.dp, height = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.65f),
            contentColor = MaterialTheme.colorScheme.onError
        )
    ) {
        Text(
            stringResource(R.string.delete_account_button),
        )
    }
}



