package ru.itis.notes.domain

class ValidatePasswordUseCase {

    operator fun invoke(password: String): String {
        val result_msg: String
        if(password.isBlank()) {
            result_msg = "Password cannot be blank!"
        } else if(password.length < 8) {
            result_msg = "Password is too short!"
        } else {
            result_msg = "Success"
        }
        return result_msg
    }

}