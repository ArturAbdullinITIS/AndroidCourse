package ru.itis.notifications.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.itis.notifications.domain.entities.Message
import ru.itis.notifications.domain.repositories.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){
    operator fun invoke(): Flow<List<Message>> {
        return messageRepository.getAllMessages()
    }
}