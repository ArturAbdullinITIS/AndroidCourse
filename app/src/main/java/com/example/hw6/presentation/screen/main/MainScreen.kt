package com.example.hw6.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.hw6.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    onNavigateToDetails: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val query by viewModel.query.collectAsState()
    val onCommand = remember(viewModel) { viewModel::processCommand }

    LaunchedEffect(viewModel.snackMessage) {
        viewModel.snackMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    MainContent(
        state = state,
        query = query,
        onCommand = onCommand,
        onNavigateToDetails = onNavigateToDetails,
    )
}

@Composable
private fun MainContent(
    state: MainState,
    query: String,
    onCommand: (MainCommand) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchField(
            value = query,
            onValueChange = {
                onCommand(MainCommand.InputQuery(it))
            },
            onSearch = {
                onCommand(MainCommand.SearchBooks(query))
            }
        )
        when (val currentState = state) {
            is MainState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            MainState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.what_book_are_you_interested_in),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            MainState.Searching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainState.Success -> {
                if (currentState.books.isNotEmpty()) {
                    LazyColumn{
                        itemsIndexed(
                            items = currentState.books,
                            key = { _, book -> book.id }
                        ) { _, book ->
                            BookItem(
                                book = book,
                                onClick = { onNavigateToDetails(book.id) },
                            )
                        }

                        if (currentState.hasMorePages) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (currentState.isLoadingNextPage) {
                                        CircularProgressIndicator()
                                    } else {
                                        Button(
                                            onClick = {
                                                onCommand(MainCommand.LoadNextPage)
                                            }
                                        ) {
                                            Text(text = stringResource(R.string.load_more))
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.nothing_found),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
