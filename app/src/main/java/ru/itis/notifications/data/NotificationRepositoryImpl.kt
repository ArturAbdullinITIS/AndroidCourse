package ru.itis.notifications.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl: NotificationRepository {

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
            oldList + notification
        }
    }

    override suspend fun updateNotification(id: Int, content: String): Boolean {
        var updated = false
        notifications.update { oldList ->
            oldList.map {
                if (it.id == id) {
                    updated = true
                    it.copy(
                        content = content
                    )
                } else {
                    it
                }
            }
        }
        return updated
    }

    override suspend fun cancelAllNotifications() {
        notifications.update { emptyList() }
    }

    override suspend fun notificationExists(id: Int): Boolean {
        return notifications.value.any {
            it.id == id
        }
    }
}