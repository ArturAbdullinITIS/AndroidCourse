package com.example.hw6.util

sealed class Result<out T> {
    data class Success<T>(val data: T, val source: DataSource) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}

enum class DataSource(val message: String) {
    DATABASE("from database"),
    API("from api")
}