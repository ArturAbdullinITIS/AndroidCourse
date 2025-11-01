package ru.itis.notifications.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.domain.repositories.NotificationRepository
import ru.itis.notifications.utils.AppNotificationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationManager: AppNotificationManager
) : NotificationRepository {

    private val notifications = MutableStateFlow<List<Notification>>(listOf())

    override suspend fun createNotification(
        title: String,
        content: String,
        priority: NotificationPriority,
        isExpandable: Boolean,
        shouldOpenApp: Boolean,
        hasReplyAction: Boolean
    ) {
        notifications.update { oldList ->
            val notification = Notification(
                id = oldList.size,
                title = title,
                content = content,
                priority = priority,
                isExpandable = isExpandable,
                shouldOpenApp = shouldOpenApp,
                hasReplyAction = hasReplyAction
            )

            notificationManager.showNotification(notification)

            Log.d("NotificationRepository", "Created notification: $notification")

            oldList + notification
        }
    }

    override suspend fun updateNotification(id: Int, content: String): Boolean {
        var updated = false
        notifications.update { oldList ->
            oldList.map { notification ->
                if (notification.id == id) {
                    updated = true
                    val updatedNotification = notification.copy(content = content)

                    notificationManager.showNotification(updatedNotification)

                    updatedNotification
                } else {
                    notification
                }
            }
        }
        return updated
    }

    override suspend fun cancelAllNotifications() {
        notificationManager.cancelAllNotifications()
        notifications.update { emptyList() }
    }

    override suspend fun notificationExists(id: Int): Boolean {
        return notificationManager.notificationExists(id)
    }
}
