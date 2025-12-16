package ru.itis.practice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="users")
data class UserDbModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="email")
    val email: String,
    @ColumnInfo(name="password_hash")
    val passwordHash: String,
    @ColumnInfo(name="is_active")
    val isActive: Boolean = true
)