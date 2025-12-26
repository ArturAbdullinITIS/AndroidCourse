package ru.itis.practice.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.practice.R
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.repository.MoviesRepository
import ru.itis.practice.util.ResourceProvider
import javax.inject.Inject

data class MovieValidationResult(
    val isValid: Boolean,
    val errors: Map<MovieField, String> = emptyMap(),
    val dbError: String? = null
)


enum class MovieField {
    TITLE,
    DESCRIPTION,
    YEAR,
    RATING
}

class AddMovieUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val resourceProvider: ResourceProvider
) {

    suspend operator fun invoke(
        title: String,
        description: String,
        year: String,
        rating: String
    ): MovieValidationResult = withContext(Dispatchers.Default){
        val errors = mutableMapOf<MovieField, String>()

        if (title.isBlank()) {
            errors[MovieField.TITLE] = resourceProvider.getString(R.string.title_is_required)
        } else if (title.length < 2) {
            errors[MovieField.TITLE] = resourceProvider.getString(R.string.title_must_be_at_least_2_characters)
        } else if (title.length > 100) {
            errors[MovieField.TITLE] = resourceProvider.getString(R.string.title_too_long_max_100_chars)
        }

        if (description.isBlank()) {
            errors[MovieField.DESCRIPTION] = resourceProvider.getString(R.string.description_is_required)
        } else if (description.length < 10) {
            errors[MovieField.DESCRIPTION] = resourceProvider.getString(R.string.description_too_short_min_10_chars)
        } else if (description.length > 500) {
            errors[MovieField.DESCRIPTION] = resourceProvider.getString(R.string.description_too_long_max_500_chars)
        }

        val ratingValue = rating.toDoubleOrNull()
        if (rating.isBlank()) {
            errors[MovieField.RATING] = resourceProvider.getString(R.string.rating_is_required)
        } else if (ratingValue == null) {
            errors[MovieField.RATING] = resourceProvider.getString(R.string.invalid_rating_format)
        } else if (ratingValue < 0.0) {
            errors[MovieField.RATING] = resourceProvider.getString(R.string.rating_cannot_be_negative)
        } else if (ratingValue > 10.0) {
            errors[MovieField.RATING] = resourceProvider.getString(R.string.rating_cannot_exceed_10_0)
        }

        val yearValue = year.toIntOrNull()
        if (year.isBlank()) {
            errors[MovieField.YEAR] = resourceProvider.getString(R.string.year_is_required)
        } else if (year.length != 4) {
            errors[MovieField.YEAR] = resourceProvider.getString(R.string.year_must_be_4_digits)
        } else if (yearValue == null) {
            errors[MovieField.YEAR] = resourceProvider.getString(R.string.invalid_year_format)
        } else if (yearValue < 1900) {
            errors[MovieField.YEAR] = resourceProvider.getString(R.string.year_too_old_min_1900)
        } else if (yearValue > 2030) {
            errors[MovieField.YEAR] = resourceProvider.getString(R.string.year_in_the_future_max_2030)
        }

        if (errors.isNotEmpty()) {
            return@withContext MovieValidationResult(
                isValid = false,
                errors = errors
            )
        }

        return@withContext runCatching {
            moviesRepository.addMovie(
                title = title,
                description = description,
                year = yearValue!!,
                rating = ratingValue!!
            )
        }.fold(
            onSuccess = {
                MovieValidationResult(isValid = true)
            },
            onFailure = {
                val message = resourceProvider.getString(R.string.failed_to_add_movie)
                MovieValidationResult(
                    isValid = false,
                    dbError = message
                )
            }
        )
    }
}
