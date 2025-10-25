package ru.itis.notes.presentation.screens.second

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.practice.R

@Composable
fun NotesScreen(
    email: String,
    modifier: Modifier = Modifier,
    viewModel: NotesScreenViewModel = remember {NotesScreenViewModel()},
    onCreateNoteButton: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            CreateNoteButton(
                modifier = Modifier,
                onClick = onCreateNoteButton
            )

        }

    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = email,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                fontSize = 22.sp
            )

            DropdownTheme(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                expanded = expanded,
                onExpandedChange = { expanded = it }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.notes) { note ->
                    NoteCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        note = note,
                        backgroundColor = MaterialTheme.colorScheme.surface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
