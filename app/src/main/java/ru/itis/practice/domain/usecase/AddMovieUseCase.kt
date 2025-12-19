package ru.itis.practice.domain.usecase

import ru.itis.practice.domain.repository.MoviesRepository
import javax.inject.Inject

data class MovieValidationResult(
    val isValid: Boolean,
    val errors: Map<String, String> = emptyMap()
)

class AddMovieUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(
        title: String,
        description: String,
        year: String,
        rating: String
    ): MovieValidationResult {
        val errors = mutableMapOf<String, String>()

        if (title.isBlank()) {
            errors["title"] = "Title is required"
        } else if (title.length < 2) {
            errors["title"] = "Title must be at least 2 characters"
        } else if (title.length > 100) {
            errors["title"] = "Title too long (max 100 chars)"
        }

        if (description.isBlank()) {
            errors["description"] = "Description is required"
        } else if (description.length < 10) {
            errors["description"] = "Description too short (min 10 chars)"
        } else if (description.length > 500) {
            errors["description"] = "Description too long (max 500 chars)"
        }

        val ratingValue = rating.toDoubleOrNull()
        if (rating.isBlank()) {
            errors["rating"] = "Rating is required"
        } else if (ratingValue == null) {
            errors["rating"] = "Invalid rating format"
        } else if (ratingValue < 0.0) {
            errors["rating"] = "Rating cannot be negative"
        } else if (ratingValue > 10.0) {
            errors["rating"] = "Rating cannot exceed 10.0"
        }

        val yearValue = year.toIntOrNull()
        if (year.isBlank()) {
            errors["year"] = "Year is required"
        } else if (year.length != 4) {
            errors["year"] = "Year must be 4 digits"
        } else if (yearValue == null) {
            errors["year"] = "Invalid year format"
        } else if (yearValue < 1900) {
            errors["year"] = "Year too old (min 1900)"
        } else if (yearValue > 2030) {
            errors["year"] = "Year in the future (max 2030)"
        }

        return if (errors.isEmpty()) {
            moviesRepository.addMovie(
                title = title,
                description = description,
                year = yearValue!!,
                rating = ratingValue!!
            )
            MovieValidationResult(isValid = true)
        } else {
            MovieValidationResult(isValid = false, errors = errors)
        }
    }
}
