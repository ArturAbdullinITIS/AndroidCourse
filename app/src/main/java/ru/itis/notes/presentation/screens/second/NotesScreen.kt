package ru.itis.notes.presentation.screens.second

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.itis.notes.presentation.navigation.Screen

@Composable
fun NotesScreen(
    email: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = email,
        modifier = modifier.padding(32.dp)
    )

}