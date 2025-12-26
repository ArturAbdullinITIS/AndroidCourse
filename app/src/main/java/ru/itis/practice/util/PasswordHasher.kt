package ru.itis.practice.util

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    private const val LOG_ROUNDS = 12


    fun hash(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(LOG_ROUNDS, password.toCharArray())
    }

    fun verify(password: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash.toByteArray()).verified
    }
}