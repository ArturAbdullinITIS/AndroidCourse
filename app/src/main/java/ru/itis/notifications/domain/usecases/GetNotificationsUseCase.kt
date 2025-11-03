package ru.itis.notifications.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
){

    operator fun invoke(): Flow<List<Notification>>  {
        return notificationRepository.getAllNotifications()
    }
}