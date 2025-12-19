package ru.itis.practice.domain.entity

data class Movie (
    val id: Int,
    val userId: Int?,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val rating: Double
)