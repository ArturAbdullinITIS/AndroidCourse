package ru.itis.practice.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import ru.itis.notifications.R
import ru.itis.practice.utils.mapToCoroutineDispatchers
import ru.itis.practice.utils.mapToCoroutineLaunch
import java.io.IOException
import javax.inject.Inject

class CoroutinesUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var currentJob: Job? = null
    private var completedCount: Int = 0
    suspend fun launchAllCoroutines(
        count: Int,
        pool: Dispatchers,
        isParallel: Boolean,
        delayedLaunch: Boolean,
        onProgress: (completed: Int, total: Int) -> Unit,
        onException: (Exception) -> Unit,
    ): List<String> =
        coroutineScope {
            completedCount = 0
            currentJob = coroutineContext[Job]
            fun report() {
                completedCount++
                onProgress(completedCount, count)
            }

            if (isParallel) {
                val deffereds = List(count) { index ->
                    async(
                        context = pool.mapToCoroutineDispatchers(),
                        start = mapToCoroutineLaunch(delayedLaunch)
                    ) {
                        execCoroutine(index, onException).also { report() }
                    }
                }
                deffereds.awaitAll()
            } else {
                val res = mutableListOf<String>()
                for (index in 0 until count) {
                    ensureActive()
                    res.add(execCoroutine(index, onException))
                    report()
                }
                res
            }
        }
    fun cancellAllCoroutines() {
        currentJob?.cancel()
        currentJob = null
    }

    fun getCompletedCount(): Int{
        return completedCount
    }


    private suspend fun execCoroutine(index: Int, onException: (Exception) -> Unit): String {
        println(context.getString(R.string.coroutine_is_running, index))
        val delayTime = (1000L..10000L).random()
        val startTime = System.currentTimeMillis()
        delay(delayTime)
        val execTime = System.currentTimeMillis() - startTime

        if(execTime >= 7000) {
            val exception = throwRandException(index)
            onException(exception)
        }
        return context.getString(R.string.coroutine_is_completed, index)
    }


    private fun throwRandException(index: Int): Exception {
        val random = (1..3).random()
        return when(random) {
            1 -> IOException(context.getString(R.string.coroutine_is_taking_too_long, index))
            2 -> ArrayIndexOutOfBoundsException(
                context.getString(
                    R.string.coroutine_is_taking_too_long_2,
                    index
                ))
            3 -> ClassNotFoundException(
                context.getString(
                    R.string.coroutine_is_taking_too_long_3,
                    index
                ))
            else -> RuntimeException()
        }
    }


}


