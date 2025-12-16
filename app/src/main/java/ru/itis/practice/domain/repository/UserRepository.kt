package ru.itis.practice.domain.repository

import ru.itis.practice.domain.entity.User


interface UserRepository {

    suspend fun registerUser(email: String, password: String)

    suspend fun loginUser(email: String, password: String): Boolean

    fun getActiveUserId(): Int?

    suspend fun logout()


}