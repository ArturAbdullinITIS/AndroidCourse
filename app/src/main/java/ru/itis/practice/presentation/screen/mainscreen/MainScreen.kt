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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    Icon(
                        modifier = Modifier.padding(end = 16.dp)
                            .size(40.dp)
                            .clickable {
                                onNavigateToProfile()
                            },
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account"
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
            LazyColumn {
                if (state.movies.isEmpty()) {
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
                    items(
                        items = state.movies
                    ) { movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            onDoubleClick = {
                                viewModel.processCommand(MainScreenCommand.DeleteCard)
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

            }

        }

    }

}