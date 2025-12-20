package ru.itis.practice.presentation.screen.profile

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.practice.R


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
fun UsernameEditRow(
    username: String,
    onUsernameChange: (String) -> Unit,
    onSaveUsername: () -> Unit,
    errorMessage: String,
    isSuccess: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UsernameTextField(
            value = username,
            onValueChange = onUsernameChange,
            errorMessage = errorMessage,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onSaveUsername,
            modifier = Modifier.size(56.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.save_username),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.username_label)) },
        placeholder = { Text(stringResource(R.string.enter_your_username)) },
        shape = RoundedCornerShape(40.dp),
        modifier = modifier,
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
            containerColor = MaterialTheme.colorScheme.error,
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



