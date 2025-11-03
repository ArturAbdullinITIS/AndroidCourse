package ru.itis.notifications.domain.entities

data class Notification(
    val id: Int,
    val title: String = "",
    val content: String = "",
    val priority: NotificationPriority = NotificationPriority.MEDIUM,
    val isExpandable: Boolean = false,
    val shouldOpenApp: Boolean = false,
    val hasReplyAction: Boolean = false
)