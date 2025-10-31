package ru.itis.notifications.domain.repositories

import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.entities.NotificationPriority

interface NotificationRepository {
    suspend fun createNotification(
        title: String,
        content: String,
        priority: NotificationPriority,
        isExpandable: Boolean,
        shouldOpenApp: Boolean,
        hasReplyAction: Boolean
    )
    suspend fun updateNotification(id: Int, content: String): Boolean
    suspend fun cancelAllNotifications()
    suspend fun notificationExists(id: Int): Boolean
}