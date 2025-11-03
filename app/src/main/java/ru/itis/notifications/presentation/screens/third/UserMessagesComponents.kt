package ru.itis.notifications.presentation.screens.third

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.notifications.R
import ru.itis.notifications.domain.entities.Message


@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .height(56.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(R.string.message_placeholder))
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun SendMessageButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = stringResource(R.string.send_button_description)
        )
    }
}


@Composable
fun Message(
    modifier: Modifier = Modifier,
    bgColor: androidx.compose.ui.graphics.Color,
    message: Message
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = if(message.isFromNotification) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
            .padding(8.dp)
    ) {
        Text(
            text = message.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}
