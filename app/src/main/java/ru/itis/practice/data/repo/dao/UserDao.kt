package ru.itis.practice.data.repo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.practice.data.model.UserDbModel


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(userDbModel: UserDbModel): Long
    @Query("UPDATE users SET is_active = 0 WHERE is_active = 1")
    suspend fun clearActiveUser()

    @Query("SELECT * FROM users WHERE email = :email AND deletedAt IS NULL LIMIT 1")
    suspend fun findUserByEmail(email: String): UserDbModel?


    @Query("SELECT email FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserEmail(userId: Int): String

    @Query("SELECT id FROM users WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveUserId(): Int?

    @Query("UPDATE users SET is_active = 1 WHERE id = :userId")
    suspend fun setActiveUser(userId: Int)


    @Query("UPDATE users SET user_name = :userName WHERE id = :userId")
    suspend fun setUserName(userId: Int, userName: String)

    @Query("SELECT user_name FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserName(userId: Int): String

    @Query("UPDATE users SET deletedAt = :timestamp WHERE id = :userId")
    suspend fun softDeleteUser(userId: Int?, timestamp: Long)

    @Query("UPDATE users SET deletedAt = NULL WHERE id = :userId")
    suspend fun restoreUser(userId: Int)


    @Query("DELETE FROM users WHERE deletedAt IS NOT NULL AND deletedAt < :sevenDays")
    suspend fun deleteOldUsers(sevenDays: Long)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun hardDeleteUser(userId:Int)

    @Query("SELECT * FROM users WHERE email = :email AND deletedAt IS NOT NULL LIMIT 1")
    suspend fun findDeletedByEmail(email: String): UserDbModel?
}