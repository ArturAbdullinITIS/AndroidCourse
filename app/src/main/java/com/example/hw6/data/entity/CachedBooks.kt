package com.example.hw6.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cached_books")
data class CachedBooks(
    @PrimaryKey
    val query: String,
    val responseJson: String,
    val timestamp: Long,
    val expiry: Long
)