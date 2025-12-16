package ru.itis.practice.data.repo.dao

import androidx.core.view.WindowInsetsCompat
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

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): UserDbModel?

    @Query("UPDATE users SET is_active = 1 WHERE id = :userId")
    suspend fun setActiveUser(userId: Int)
}