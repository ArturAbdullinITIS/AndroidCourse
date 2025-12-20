package ru.itis.practice.data.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.itis.practice.domain.repository.UserRepository

class DeleteUsersWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository
): CoroutineWorker(context, workerParams){
    override suspend fun doWork(): Result {
        userRepository.deleteOldUsers()
        return Result.success()
    }
}