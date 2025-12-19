package ru.itis.practice.presentation.screen.create

import android.R.attr.enabled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TitleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    maxLength: Int = 25,
    modifier: Modifier = Modifier
) {
    val maxChars = maxLength
    val remainingChars = maxChars - value.length
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxChars) {
                onValueChange(newValue)
            }
        },
        label = { Text("Title") },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Text(
                    text = "$remainingChars/$maxChars",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        remainingChars < 0 -> MaterialTheme.colorScheme.error
                        remainingChars < 50 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        isError = errorMessage.isNotBlank() || value.length > maxChars,
        placeholder = { Text("type movie title") }
    )
}

@Composable
fun DescriptionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    maxLength: Int = 150,
    modifier: Modifier = Modifier
) {
    val maxChars = maxLength
    val remainingChars = maxChars - value.length

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxChars) {
                onValueChange(newValue)
            }
        },
        label = { Text("Description") },
        shape = RoundedCornerShape(16.dp),
        minLines = 3,
        maxLines = 5,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Text(
                    text = "$remainingChars/$maxChars",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        remainingChars < 0 -> MaterialTheme.colorScheme.error
                        remainingChars < 50 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        isError = errorMessage.isNotBlank() || value.length > maxChars,
        placeholder = { Text("type movie description") }
    )
}

@Composable
fun RatingTextField(
    rating: String,
    onRatingChange: (String) -> Unit,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = rating,
        onValueChange = { newValue ->
            val filtered = newValue.filter { it.isDigit() || it == '.' }
            if (filtered.count { it == '.' } <= 1 && filtered.length <= 4) {
                onRatingChange(filtered)
            }
        },
        label = { Text("Rating (0.0 - 10.0)") },
        placeholder = { Text("type movie rating") },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        maxLines = 1,
        isError = errorMessage.isNotBlank(),
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                val ratingValue = rating.toFloatOrNull() ?: 0f
                val color = when {
                    ratingValue > 10f || ratingValue < 0f -> MaterialTheme.colorScheme.error
                    ratingValue >= 7f -> MaterialTheme.colorScheme.primary
                    ratingValue >= 4f -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            }
        }
    )
}

@Composable
fun YearInput(
    year: String,
    onYearChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    OutlinedTextField(
        value = year,
        onValueChange = { newValue ->
            val filtered = newValue.filter { it.isDigit() }.take(4)
            onYearChange(filtered)
        },
        label = { Text("Release Year") },
        placeholder = { Text("type release year") },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        maxLines = 1,
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        isError = errorMessage.isNotBlank()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.size(width = 300.dp, height = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(text = "Submit")
    }
}
