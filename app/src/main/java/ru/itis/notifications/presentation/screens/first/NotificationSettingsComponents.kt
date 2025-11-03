package ru.itis.notifications.presentation.screens.first

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.R


@Composable
fun TitleField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(R.string.notification_title_placeholder))
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun ContentField(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    modifier: Modifier = Modifier,
    selectedPriority: NotificationPriority,
    onPrioritySelected: (NotificationPriority) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedPriority.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.select_priority)) },
            shape = RoundedCornerShape(10.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            NotificationPriority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onPrioritySelected(priority)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun ExpandableSwitch(
    modifier: Modifier = Modifier,
    isExpandable: Boolean,
    onExpandableChanged: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.expandable_notification),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isExpandable,
            onCheckedChange = onExpandableChanged,
            enabled = enabled
        )
    }
}

@Composable
fun OpenAppSwitch(
    modifier: Modifier = Modifier,
    shouldOpenApp: Boolean,
    onOpenAppChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.open_app_on_click),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = shouldOpenApp,
            onCheckedChange = onOpenAppChanged
        )
    }
}

@Composable
fun ReplyActionSwitch(
    modifier: Modifier = Modifier,
    hasReplyAction: Boolean,
    onReplyActionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.add_reply_action),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = hasReplyAction,
            onCheckedChange = onReplyActionChanged
        )
    }
}

@Composable
fun CreateNotificationButton(
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
        Text(stringResource(R.string.create_notification))
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
