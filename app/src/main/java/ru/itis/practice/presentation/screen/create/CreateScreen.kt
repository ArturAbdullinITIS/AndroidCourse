package ru.itis.practice.presentation.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputDescription
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputRating
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputTitle
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputYear
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.Submit

@Composable
fun CreateScreen(
    onNavigateToMain: () -> Unit
) {
    CreateContent(onNavigateToMain = onNavigateToMain)
}

@Composable
fun CreateContent(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CreateViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            onNavigateToMain()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Your Movie",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    TitleTextField(
                        value = state.title,
                        onValueChange = { viewModel.processCommand(InputTitle(it)) },
                        errorMessage = state.titleError ?: ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    YearInput(
                        errorMessage = state.yearError ?: "",
                        year = state.releaseYear,
                        onYearChange = { viewModel.processCommand(InputYear(it)) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RatingTextField(
                        rating = state.rating,
                        onRatingChange = { viewModel.processCommand(InputRating(it)) },
                        errorMessage = state.ratingError ?: ""
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DescriptionTextField(
                        value = state.description,
                        onValueChange = { viewModel.processCommand(InputDescription(it)) },
                        errorMessage = state.descriptionError ?: "",)

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SubmitButton(
                        onClick = { viewModel.processCommand(Submit) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
