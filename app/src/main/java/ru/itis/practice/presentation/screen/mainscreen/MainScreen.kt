package ru.itis.practice.presentation.screen.mainscreen

import android.R.attr.onClick
import android.text.TextUtils.isEmpty
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.toUri
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import ru.itis.practice.R
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.repository.MoviesRepository
import ru.itis.practice.presentation.screen.create.CreateViewModel


@Composable
fun MainScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToProfile: () -> Unit
) {

    MainScreenContent(
        onNavigateToCreate = onNavigateToCreate,
        onNavigateToProfile = onNavigateToProfile
    )
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
    val listState = rememberLazyListState()

    LaunchedEffect(state.image) {
        viewModel.processCommand(MainScreenCommand.RefreshImage)
    }

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
                    contentDescription = stringResource(R.string.add_movie_screen)
                )
            }
        },
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.large
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Your Movies",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = { showBottomSheet = true }) {
                            Box(modifier = Modifier.size(50.dp)) {
                                Icon(
                                    Icons.Default.Sort,
                                    contentDescription = stringResource(R.string.sort),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (state.image.isBlank()) {
                            IconButton(onClick = onNavigateToProfile) {
                                Box(modifier = Modifier.size(50.dp)) {
                                    Icon(
                                        Icons.Default.AccountCircle,
                                        contentDescription = stringResource(R.string.profile_screen),
                                        modifier = Modifier
                                            .size(50.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { onNavigateToProfile() }
                                    .clip(CircleShape)
                                    .padding(4.dp)
                            ) {
                                AsyncImage(
                                    model = state.image,
                                    contentDescription = stringResource(R.string.profile_picture_screen),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
            LazyColumn(
                state = listState
            ) {
                if (sortedMovies.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(
                            text = stringResource(R.string.no_movies_available_click_the_button_to_add_a_new_movie),
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
            title = { Text(stringResource(R.string.delete_movie)) },
            text = { Text(stringResource(R.string.delete_alert, movie.title)) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.processCommand(MainScreenCommand.ConfirmDelete) }
                ) { Text(stringResource(R.string.delete_alert_button)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.processCommand(MainScreenCommand.CancelDelete) }
                ) { Text(stringResource(R.string.cancel_alert_button)) }
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