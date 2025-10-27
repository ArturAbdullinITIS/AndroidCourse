package ru.itis.notes.presentation.utils



object RegEx {
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.+)(\\.)(.+)".toRegex()
}