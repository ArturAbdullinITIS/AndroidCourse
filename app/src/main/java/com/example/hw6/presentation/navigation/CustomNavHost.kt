package com.example.hw6.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hw6.R
import com.example.hw6.presentation.screen.details.DetailsScreen
import com.example.hw6.presentation.screen.main.MainScreen
import com.example.hw6.util.CrashlyticsInit
import java.util.Map.entry

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomNavHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberSaveable {
        mutableStateListOf<Route>(Main)
    }

    val currentRoute = backStack.lastOrNull() ?: Main
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            is Details -> CrashlyticsInit.logDetails(currentRoute.bookId)
            Main -> CrashlyticsInit.logScreen("Main")
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute !is Details){
                TopAppBar(
                    title = {
                        Text(
                            text = getRouteTitle(currentRoute)
                        )
                    },
                )
            }
            else {
                TopAppBar(
                    title = {
                        Text("Book Details")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                backStack.removeLastOrNull()
                            }
                        ) {
                            Icon(
                                modifier = Modifier,
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.arrow_back)
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = modifier.padding(innerPadding),
            backStack = backStack,
            onBack = {
                if(backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<Main> {
                    MainScreen(
                        onNavigateToDetails = { bookId ->
                            backStack.add(Details(bookId))
                        },
                        snackbarHostState = snackbarHostState
                    )
                }
                entry<Details> { route ->
                    DetailsScreen(
                        bookId = route.bookId
                    )
                }
            }
        )
    }

}


private fun getRouteTitle(route: Route): String {
    return when(route) {
        is Details -> "Details"
        Main -> "Main"
    }
}

