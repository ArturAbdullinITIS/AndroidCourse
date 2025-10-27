package ru.itis.notes.presentation.screens.second

import android.R.attr.enabled
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.notes.domain.Note
import ru.itis.notes.presentation.ui.theme.AppTheme
import ru.itis.notes.presentation.ui.theme.ThemeManager
import ru.itis.practice.R

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    note: Note,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = note.content,
            fontSize = 16.sp,
            maxLines = 3,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CreateNoteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_note_description)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTheme(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val currentTheme = ThemeManager.currentTheme

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        Button(
            onClick = { onExpandedChange(true) },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.choose_theme_button)
            )
            Text(
                text = when (currentTheme) {
                    AppTheme.RED -> stringResource(R.string.red_theme)
                    AppTheme.BLUE -> stringResource(R.string.blue_theme)
                    AppTheme.GREEN -> stringResource(R.string.green_theme)
                    AppTheme.SYSTEM -> stringResource(R.string.system_theme)
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.red_theme)) },
                onClick = {
                    ThemeManager.setTheme(AppTheme.RED)
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.blue_theme)) },
                onClick = {
                    ThemeManager.setTheme(AppTheme.BLUE)
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.green_theme)) },
                onClick = {
                    ThemeManager.setTheme(AppTheme.GREEN)
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.system_theme)) },
                onClick = {
                    ThemeManager.setTheme(AppTheme.SYSTEM)
                    onExpandedChange(false)
                }
            )
        }
    }
}