package com.example.hw6.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hw6.data.entity.CachedBooks

@Database(
    entities = [CachedBooks::class],
    version = 2,
)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}