package ru.itis.notifications.presentation.screens.second

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.notifications.R


@Composable
fun NotificationIDField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(R.string.notification_id_placeholder))
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun NotificationContentField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(R.string.notification_content_placeholder))
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun UpdateNotificationButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(stringResource(R.string.update_notification))
    }
}

@Composable
fun ClearAllNotificationsButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(stringResource(R.string.clear_notifications))
    }
}

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    if (message.isNotBlank()) {
        Text(
            text = message,
            modifier = modifier.fillMaxWidth(),
            color = androidx.compose.material3.MaterialTheme.colorScheme.error
        )
    }
}