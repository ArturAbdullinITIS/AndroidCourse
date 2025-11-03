package ru.itis.notifications.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.itis.notifications.domain.entities.Message

interface MessageRepository {
    fun getAllMessages(): Flow<List<Message>>
    suspend fun addMessage(content: String, isFromNotification: Boolean)
}