package ru.itis.notifications.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notifications.domain.entities.Message
import ru.itis.notifications.domain.repositories.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(): MessageRepository {

    private val messages = MutableStateFlow<List<Message>>(listOf())


    override fun getAllMessages(): Flow<List<Message>> {
        return messages.asStateFlow()
    }

    override suspend fun addMessage(content: String, isFromNotification: Boolean) {
        messages.update { oldList ->
            val message = Message(
                id = oldList.size,
                content = content,
                timestamp = System.currentTimeMillis(),
                isFromNotification = isFromNotification
            )
            oldList + message
        }
    }
}