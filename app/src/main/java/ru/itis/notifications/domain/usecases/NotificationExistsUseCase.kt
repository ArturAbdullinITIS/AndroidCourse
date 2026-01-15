package ru.itis.notifications.domain.usecases

import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class NotificationExistsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return notificationRepository.notificationExists(id)
    }
}