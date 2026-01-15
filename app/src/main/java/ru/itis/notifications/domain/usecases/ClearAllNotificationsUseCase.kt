package ru.itis.notifications.domain.usecases

import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class ClearAllNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() {
        notificationRepository.cancelAllNotifications()
    }
}