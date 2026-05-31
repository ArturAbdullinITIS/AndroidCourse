package com.example.hw6.presentation.screen.details

import android.R.attr.fontWeight
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.hw6.R


@Composable
fun DetailsScreen(bookId: String) {
    DetailsContent(bookId = bookId)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel(
        key = bookId,
        creationCallback = { factory: DetailsViewModel.Factory ->
            factory.create(bookId = bookId)
        }
    )
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.errorMessage ?: stringResource(R.string.unknown_error),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else if (state.bookItem != null) {
            state.bookItem?.let { book ->
                BookImage(
                    imageUrl = book.thumbnail
                )
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.authors?.joinToString(", ")
                        ?: stringResource(R.string.unknown_author),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = book.description ?: stringResource(R.string.no_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
