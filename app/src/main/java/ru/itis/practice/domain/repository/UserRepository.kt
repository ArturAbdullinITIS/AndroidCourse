package ru.itis.practice.domain.repository

import ru.itis.practice.domain.entity.User


interface UserRepository {

    suspend fun registerUser(email: String, password: String): Boolean

    suspend fun loginUser(email: String, password: String): Boolean

    suspend fun getActiveUserId(): Int?

    suspend fun logout()


}