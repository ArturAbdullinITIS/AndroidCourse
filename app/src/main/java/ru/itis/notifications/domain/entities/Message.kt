package ru.itis.notifications.domain.entities

data class Message(
    val id: Int,
    val content: String,
    val timestamp: Long,
    val isFromNotification: Boolean = false
)