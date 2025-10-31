package ru.itis.notifications.domain.usecases

import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class CreateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
){
    suspend operator fun invoke(
        title: String,
        content: String,
        priority: NotificationPriority,
        isExpandable: Boolean,
        shouldOpenApp: Boolean,
        hasReplyAction: Boolean
    ) {
        if(title.isBlank()){
            throw Exception("Title cannot be blank!")
        }
        notificationRepository.createNotification(
            title = title,
            content = content,
            priority = priority,
            isExpandable = isExpandable,
            shouldOpenApp = shouldOpenApp,
            hasReplyAction = hasReplyAction
        )

    }
}