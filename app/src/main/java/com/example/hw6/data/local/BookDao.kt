package com.example.hw6.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hw6.data.entity.CachedBooks
import com.example.hw6.data.remote.dto.BooksResponse

@Dao
interface BookDao {

    @Query("SELECT * from cached_books where `query` = :query")
    suspend fun getCachedBooks(query: String): CachedBooks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBooksToCache(cachedBooks: CachedBooks)

    @Query("DELETE FROM cached_books where expiry < :timeCurrent")
    suspend fun deleteExpiredCache(timeCurrent: Long)
}