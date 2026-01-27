package ru.itis.practice.domain

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import ru.itis.notifications.R
import ru.itis.practice.utils.ResManager
import ru.itis.practice.utils.mapToCoroutineDispatchers
import ru.itis.practice.utils.mapToCoroutineLaunch
import java.io.IOException
import javax.inject.Inject

class CoroutinesUseCase @Inject constructor(
    private val resManager: ResManager
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
        withContext(pool.mapToCoroutineDispatchers()) {
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
                        try {
                            execCoroutine(index, onException).also { report() }
                        } catch (e: Exception) {
                            onException(e)
                            ""
                        }

                    }
                }
                deffereds.awaitAll()
            } else {
                val res = mutableListOf<String>()
                for (index in 0 until count) {
                    ensureActive()
                    try {
                        res.add(execCoroutine(index, onException))
                    } catch (e: Exception) {
                        onException(e)
                    }
                    report()
                }
                res
            }
        }
    fun cancelAllCoroutines() {
        currentJob?.cancel()
        currentJob = null
    }

    fun getCompletedCount(): Int{
        return completedCount
    }


    private suspend fun execCoroutine(index: Int, onException: (Exception) -> Unit): String {
        println(resManager.getString(R.string.coroutine_is_running))
        val delayTime = (1000L..10000L).random()
        val startTime = System.currentTimeMillis()
        delay(delayTime)
        val execTime = System.currentTimeMillis() - startTime

        if (execTime >= 7000 && (0..100).random() < 30) {
            val randomException = throwRandException(index)
            onException(randomException)
            throw randomException
        }
        return resManager.getString(R.string.coroutine_is_completed)
    }


    private fun throwRandException(index: Int): Exception {
        val random = (1..3).random()
        return when(random) {
            1 -> IOException(resManager.getString(R.string.coroutine_is_taking_too_long))
            2 -> ArrayIndexOutOfBoundsException(
                resManager.getString(
                    R.string.coroutine_is_taking_too_long_2,

                ))
            3 -> ClassNotFoundException(
                resManager.getString(
                    R.string.coroutine_is_taking_too_long_3,
                ))
            else -> RuntimeException()
        }
    }


}


