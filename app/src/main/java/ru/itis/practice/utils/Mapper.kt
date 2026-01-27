package ru.itis.practice.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import ru.itis.practice.domain.Dispatchers


fun Dispatchers.mapToCoroutineDispatchers(): CoroutineDispatcher {
    return when(this) {
        Dispatchers.DEFAULT -> kotlinx.coroutines.Dispatchers.Default
        Dispatchers.IO -> kotlinx.coroutines.Dispatchers.IO
        Dispatchers.MAIN -> kotlinx.coroutines.Dispatchers.Main
        Dispatchers.UNCONFINED -> kotlinx.coroutines.Dispatchers.Unconfined
    }
}

fun mapToCoroutineLaunch(isDelayed: Boolean): CoroutineStart {
    return when(isDelayed) {
        true -> CoroutineStart.LAZY
        false -> CoroutineStart.DEFAULT
    }
}