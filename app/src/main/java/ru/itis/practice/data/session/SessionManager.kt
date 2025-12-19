package ru.itis.practice.data.session

import ru.itis.practice.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionDataStore: SessionDataStore
){
    suspend fun initializeSession() {
        val activeUserId = userRepository.getActiveUserId()
        val isLoggedIn = activeUserId != null
        sessionDataStore.setLoggedIn(isLoggedIn)
    }
}