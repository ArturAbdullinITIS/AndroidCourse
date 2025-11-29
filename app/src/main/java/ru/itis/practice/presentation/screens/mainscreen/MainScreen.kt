package ru.itis.practice.presentation.screens.mainscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.itis.notifications.R
import ru.itis.practice.domain.ExceptionStatus
import java.util.Locale.getDefault
import kotlin.math.roundToInt


@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = hiltViewModel()
    val currentState by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(currentState.exceptionStatus) {
        when(currentState.exceptionStatus) {
            ExceptionStatus.IO -> {
                Toast.makeText(context, context.getString(
                    R.string.taking_too_long, ExceptionStatus.IO.name.lowercase(
                        getDefault()
                    )
                ), Toast.LENGTH_SHORT).show()
                viewModel.processCommand(MainScreenCommands.ResetStatus)
            }
            ExceptionStatus.ARRAYINDEXOUTOFBOUNDS -> {
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.taking_too_long_2,
                        ExceptionStatus.ARRAYINDEXOUTOFBOUNDS.name.lowercase(
                            getDefault()
                        )
                    ))
                viewModel.processCommand(MainScreenCommands.ResetStatus)

            }
            ExceptionStatus.CLASSNOTFOUND -> {
                viewModel.processCommand(MainScreenCommands.ResetStatus)

            }
            ExceptionStatus.NONE -> {}
            ExceptionStatus.COMPLETED -> {
                Toast.makeText(context,
                    context.getString(
                        R.string.cancelled_coroutines_count,
                        currentState.completedCoroutinesCount
                    ), Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)

            ) {
                CoroutineCountSlider(
                    modifier = modifier,
                    value = currentState.coroutinesCount.toFloat(),
                    onValueChange = { newValue ->
                        val rounded = (newValue / 5).roundToInt() * 5
                        viewModel.processCommand(MainScreenCommands.ChangeCoroutinesCount(rounded))
                    },
                    valueRange = 10f..100f,
                    steps = 18
                )
                Spacer(modifier = Modifier.height(8.dp))
                DispatchersPoolDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    selectedPool = currentState.dispatcherPool,
                    onSelected = {
                        viewModel.processCommand(MainScreenCommands.ChangePool(it))
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                IsSequencedSwitch(
                    modifier = modifier,
                    checked = !currentState.isParallel,
                    onCheckedChange = {
                        viewModel.processCommand(MainScreenCommands.ChangeParallel(!it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                IsParallelSwitch(
                    modifier = modifier,
                    checked = currentState.isParallel,
                    onCheckedChange = {
                        viewModel.processCommand(MainScreenCommands.ChangeParallel(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                IsLaunchSwitch(
                    modifier = modifier,
                    checked = currentState.delayedLaunch,
                    onCheckedChange = {
                        viewModel.processCommand(MainScreenCommands.ChangeLaunch(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                IsBackgroundSwitch (
                    modifier = modifier,
                    checked = currentState.backgroundLaunch,
                    onCheckedChange = {
                        viewModel.processCommand(MainScreenCommands.ChangeBackgroundLaunch(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (currentState.isLoading) {
                    LinearProgressIndicator(
                        progress = {
                            currentState.progress
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)

            ) {
                if (!currentState.isLoading) {
                    LaunchCoroutinesButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.processCommand(MainScreenCommands.LaunchCoroutines)
                        }
                    )
                } else {
                    StopCoroutinesButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.processCommand(MainScreenCommands.StopCoroutines)
                        }
                    )
                }

            }
        }
    }
}