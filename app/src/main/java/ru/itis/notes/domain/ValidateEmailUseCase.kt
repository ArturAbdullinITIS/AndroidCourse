package ru.itis.notes.domain

class ValidateEmailUseCase {


    operator fun invoke(email: String): String {
        val result_msg: String

        if(email.isBlank()) {
            result_msg = "Email cannot be blank!"
        } else if(!email.matches("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+\$".toRegex())) {
            result_msg = "Incorrect email form"
        } else {
            result_msg = "Success"
        }
        return result_msg
    }
}