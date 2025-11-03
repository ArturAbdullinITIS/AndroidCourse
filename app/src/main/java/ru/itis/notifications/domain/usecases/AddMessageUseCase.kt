package ru.itis.notifications.domain.usecases

import ru.itis.notifications.domain.repositories.MessageRepository
import ru.itis.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){
    suspend operator fun invoke(
        content: String,
        isFromNotification: Boolean = false
    ) {
        return messageRepository.addMessage(content, isFromNotification)
    }
}