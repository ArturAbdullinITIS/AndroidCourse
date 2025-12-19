package ru.itis.practice.presentation.screen.mainscreen

import android.text.TextUtils.isEmpty
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.repository.MoviesRepository
import ru.itis.practice.presentation.screen.create.CreateViewModel


@Composable
fun MainScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToProfile: () -> Unit
) {

    MainScreenContent(onNavigateToCreate = onNavigateToCreate, onNavigateToProfile = onNavigateToProfile)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val sortedMovies by viewModel.sortedMovies.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Movie"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Movies",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.AccountCircle, "Profile")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
            LazyColumn {
                if (sortedMovies.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(
                            text = "No movies available. Click the + button to add a new movie.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    itemsIndexed(
                        items = sortedMovies,
                        key = { index, movie -> movie.id }
                    ) { index, movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            onLongClick = {
                                viewModel.processCommand(MainScreenCommand.OnCardLongClick(movie))
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

            }

        }

    }

    state.movieToDelete?.let { movie ->
        AlertDialog(
            onDismissRequest = { viewModel.processCommand(MainScreenCommand.CancelDelete) },
            title = { Text("Delete Movie") },
            text = { Text("Delete \"${movie.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.processCommand(MainScreenCommand.ConfirmDelete) }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.processCommand(MainScreenCommand.CancelDelete) }
                ) { Text("Cancel") }
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            SortBottomSheetContent(
                currentSort = state.sortOrder,
                onSortSelected = { sortOrder ->
                    viewModel.processCommand(MainScreenCommand.ChangeSortOrder(sortOrder))
                    scope.launch { sheetState.hide() }
                    showBottomSheet = false
                }
            )
        }
    }

}