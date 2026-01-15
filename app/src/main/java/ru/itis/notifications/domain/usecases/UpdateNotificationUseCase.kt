package ru.itis.notifications.domain.usecases

import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
){
    suspend operator fun invoke(
        id: Int,
        content: String,
    ): Boolean {
        return notificationRepository.updateNotification(id, content)
    }
}