package ru.itis.notes.presentation.screens.third

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.notes.presentation.navigation.Screen
import ru.itis.notes.presentation.screens.first.ContinueButton
import ru.itis.notes.presentation.screens.first.RegistrationScreenCommand


@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel = remember { CreateNoteViewModel() },
    onSaveButtonClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()


    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                TitleField(
                    modifier = Modifier.padding(innerPadding),
                    value = state.title,
                    onValueChange = {
                        viewModel.processCommand(CreateNotesCommand.InputTitle(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ContentField(
                    modifier = Modifier.padding(innerPadding),
                    value = state.content,
                    onValueChange = {
                        viewModel.processCommand(CreateNotesCommand.InputContent(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))


            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                CreateNoteButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.processCommand(CreateNotesCommand.Save)
                        onSaveButtonClick()
                    },
                    enabled = state.isEnabled

                )
            }
        }

    }


}

