package ru.itis.practice.data.mapper

import ru.itis.practice.data.model.MovieDbModel
import ru.itis.practice.domain.entity.Movie



fun MovieDbModel.toEntity(): Movie {
    return Movie(id, userId, title, description, releaseYear, rating)
}


fun List<MovieDbModel>.toEntities(): List<Movie> {
    return this.map { it.toEntity() }
}


