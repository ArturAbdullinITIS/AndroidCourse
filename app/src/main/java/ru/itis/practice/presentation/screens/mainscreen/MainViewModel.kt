package ru.itis.practice.presentation.screens.mainscreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.domain.CoroutinesUseCase
import ru.itis.practice.domain.Dispatchers
import ru.itis.practice.domain.ExceptionStatus
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coroutinesUseCase: CoroutinesUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {



    fun stopCoroutines() {
        viewModelScope.launch {
            coroutinesUseCase.cancelAllCoroutines()
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    progress = 0f,
                )
            }
            savedCoroutinesCount = _state.value.coroutinesCount

        }
    }
    fun restartCoroutines() {
        viewModelScope.launch {
            if(savedCoroutinesCount > 0) {
                coroutinesUseCase.launchAllCoroutines(
                    savedCoroutinesCount,
                    _state.value.dispatcherPool,
                    _state.value.isParallel,
                    _state.value.delayedLaunch,
                    onProgress = { completed, total ->
                        val value = completed.toFloat() / total.toFloat()
                        _state.update { state ->
                            state.copy(
                                progress = value.coerceIn(0f, 1f),
                                completedCoroutinesCount = completed
                            )
                        }
                    },
                    onException = { exception ->
                        _state.update { state ->
                            when (exception) {
                                is IOException -> state.copy(exceptionStatus = ExceptionStatus.IO)
                                is ArrayIndexOutOfBoundsException -> state.copy(exceptionStatus = ExceptionStatus.ARRAYINDEXOUTOFBOUNDS)
                                else -> state.also { resetAllSettings() }
                            }
                        }
                        Log.d("MainScreenState", "${_state.value}")
                    }
                )
                savedCoroutinesCount = 0
            }
        }
    }

    private var savedCoroutinesCount = 0


    private val _state = MutableStateFlow(MainScreenState())

    val state = _state.asStateFlow()

    private fun resetAllSettings() {
        _state.update {
            MainScreenState(
                coroutinesCount = it.coroutinesCount,
                dispatcherPool = Dispatchers.DEFAULT,
                isParallel = false,
                delayedLaunch = false,
                progress = it.progress,
                isLoading = it.isLoading
            )
        }
    }
    fun processCommand(command: MainScreenCommands) {
        when (command) {
            is MainScreenCommands.ChangeLaunch -> {
                _state.update { prState ->
                    prState.copy(
                        delayedLaunch = command.delayedLaunch
                    )
                }
            }

            is MainScreenCommands.ChangeParallel -> {
                _state.update { prState ->
                    prState.copy(
                        isParallel = command.isParallel,
                    )
                }
            }

            is MainScreenCommands.ChangePool -> {
                _state.update { prState ->
                    prState.copy(
                        dispatcherPool = command.dispatcherPool
                    )
                }
            }

            is MainScreenCommands.ChangeCoroutinesCount -> {
                _state.update { prState ->
                    prState.copy(
                        coroutinesCount = command.coroutinesCount
                    )
                }
            }

            MainScreenCommands.LaunchCoroutines -> {
                val currentState = _state.value
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            progress = 0f,
                            completedCoroutinesCount = 0,
                            exceptionStatus = ExceptionStatus.NONE
                        )
                    }
                    coroutinesUseCase.launchAllCoroutines(
                        count = _state.value.coroutinesCount,
                        pool = _state.value.dispatcherPool,
                        isParallel = _state.value.isParallel,
                        delayedLaunch = _state.value.delayedLaunch,
                        onProgress = { completed, total ->
                            val value = completed.toFloat() / total.toFloat()
                            _state.update { state -> state.copy(progress = value.coerceIn(0f, 1f)) }
                        },
                        onException = { exception ->
                            _state.update { state ->
                                when (exception) {
                                    is IOException -> state.copy(exceptionStatus = ExceptionStatus.IO)
                                    is ArrayIndexOutOfBoundsException -> state.copy(exceptionStatus = ExceptionStatus.ARRAYINDEXOUTOFBOUNDS)
                                    is ClassNotFoundException -> state.copy(exceptionStatus = ExceptionStatus.CLASSNOTFOUND).also { resetAllSettings() }
                                    else -> state
                                }
                            }
                            Log.d("MainScreenState", "${_state.value}")


                        }
                    )
                    _state.update { state -> state.copy(isLoading = false) }
                }
            }

            MainScreenCommands.ResetStatus -> {
                _state.update { state ->
                    state.copy(
                        exceptionStatus = ExceptionStatus.NONE
                    )
                }
            }

            MainScreenCommands.StopCoroutines -> {
                Log.d("MainScreen", "Coroutines stopped")
                viewModelScope.launch {
                    coroutinesUseCase.cancelAllCoroutines()
                }
                _state.update { state ->
                    val completed = coroutinesUseCase.getCompletedCount()
                    val total = state.coroutinesCount
                    val canceled = total - completed
                    state.copy(
                        isLoading = false,
                        progress = 0f,
                        completedCoroutinesCount = canceled,
                        exceptionStatus = ExceptionStatus.COMPLETED
                    )

                }

            }

            is MainScreenCommands.ChangeBackgroundLaunch -> {
                _state.update { state ->
                    state.copy(
                        backgroundLaunch = command.backgroundLaunch
                    )

                }
            }
        }
    }
}


sealed interface MainScreenCommands {

    data class ChangeCoroutinesCount(val coroutinesCount: Int) : MainScreenCommands
    data class ChangePool(val dispatcherPool: Dispatchers) : MainScreenCommands
    data class ChangeParallel(val isParallel: Boolean) : MainScreenCommands
    data class ChangeLaunch(val delayedLaunch: Boolean) : MainScreenCommands
    data class ChangeBackgroundLaunch(val backgroundLaunch: Boolean): MainScreenCommands
    data object LaunchCoroutines : MainScreenCommands
    data object ResetStatus: MainScreenCommands
    data object StopCoroutines: MainScreenCommands
}

data class MainScreenState(
    val coroutinesCount: Int = 0,
    val dispatcherPool: Dispatchers = Dispatchers.DEFAULT,
    val isParallel: Boolean = false,
    val delayedLaunch: Boolean = false,
    val backgroundLaunch: Boolean = false,
    val progress: Float = 0f,
    val isLoading: Boolean = false,
    val exceptionStatus: ExceptionStatus = ExceptionStatus.NONE,
    val completedCoroutinesCount: Int = 0
)