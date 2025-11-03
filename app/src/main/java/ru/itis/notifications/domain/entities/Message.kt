package ru.itis.notifications.domain.entities

data class Message(
    val id: Int,
    val content: String,
    val isFromNotification: Boolean = false
)