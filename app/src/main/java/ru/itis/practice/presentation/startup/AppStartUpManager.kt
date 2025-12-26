package ru.itis.practice.presentation.startup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.itis.practice.data.background.DeleteUsersWorker
import ru.itis.practice.domain.usecase.DeleteOldUsersUseCase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppStartUpManager @Inject constructor(
    private val deleteOldUsersUseCase: DeleteOldUsersUseCase
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startDeleteOldUsersWork() {
        scope.launch {
            deleteOldUsersUseCase()
        }
    }
}