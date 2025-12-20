package ru.itis.practice.domain.repository

import ru.itis.practice.data.model.UserDbModel
import ru.itis.practice.domain.entity.User


interface UserRepository {

    suspend fun registerUser(email: String, password: String): Boolean

    suspend fun loginUser(email: String, password: String): Boolean

    suspend fun getActiveUserId(): Int?

    suspend fun logout()

    suspend fun setUserName(userName: String)

    suspend fun getUserEmail(): String

    suspend fun getUsername(): String

    suspend fun getUserImage(): String

    suspend fun setSessionActive(isActive: Boolean)

    suspend fun syncSessionWithDatabase()

    suspend fun softDeleteUser()

    suspend fun restoreUser(userId: Int)

    suspend fun deleteOldUsers()

    suspend fun findUserByEmail(email: String): UserDbModel?

    suspend fun findDeletedByEmail(email: String): UserDbModel?

    suspend fun hardDeleteUser(userId: Int)

    suspend fun setActiveUser(userId: Int)

    suspend fun startDeleteOldUsersWork()

    suspend fun addImage(imagePath: String)

    suspend fun deleteImage()
}