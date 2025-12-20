package ru.itis.practice.domain.entity

data class User (
    val id: Int,
    val userName: String,
    val email: String,
    val image: String?
)