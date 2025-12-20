package ru.itis.practice.presentation.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.practice.R
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputDescription
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputRating
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputTitle
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.InputYear
import ru.itis.practice.presentation.screen.create.CreateScreenCommand.Submit

@Composable
fun CreateScreen(
    onNavigateToMain: () -> Unit,
    onNavigateBack: () -> Unit
) {
    CreateContent(onNavigateToMain = onNavigateToMain, onNavigateBack = onNavigateBack)
}

@Composable
fun CreateContent(
    onNavigateToMain: () -> Unit,
    onNavigateBack: () -> Unit,
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.add_movie),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        TitleTextField(
                            value = state.title,
                            onValueChange = { viewModel.processCommand(InputTitle(it)) },
                            errorMessage = state.titleError ?: ""
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        YearInput(
                            errorMessage = state.yearError ?: "",
                            year = state.releaseYear,
                            onYearChange = { viewModel.processCommand(InputYear(it)) }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        RatingTextField(
                            rating = state.rating,
                            onRatingChange = { viewModel.processCommand(InputRating(it)) },
                            errorMessage = state.ratingError ?: ""
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        DescriptionTextField(
                            value = state.description,
                            onValueChange = { viewModel.processCommand(InputDescription(it)) },
                            errorMessage = state.descriptionError ?: ""
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        SubmitButton(
                            onClick = { viewModel.processCommand(Submit) },
                        )
                    }
                }

                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 20.dp, start = 12.dp)
                        .size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

