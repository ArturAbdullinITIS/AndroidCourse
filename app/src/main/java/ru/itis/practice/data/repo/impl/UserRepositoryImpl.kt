package ru.itis.practice.data.repo.impl

import ru.itis.practice.data.model.UserDbModel
import ru.itis.practice.data.repo.dao.UserDao
import ru.itis.practice.domain.repository.UserRepository
import ru.itis.practice.util.PasswordHasher
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): UserRepository {

    override suspend fun registerUser(email: String, password: String) {
        val hashedPassword = PasswordHasher.hash(password)
        val userDbModel = UserDbModel(
            email = email,
            passwordHash = hashedPassword
        )

        val insertedId = userDao.registerUser(userDbModel)

        userDao.clearActiveUser()
        userDao.setActiveUser(insertedId.toInt())
    }

    override suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.findUserByEmail(email) ?: return false

        if (!PasswordHasher.verify(password, user.passwordHash)) {
            return false
        }

        userDao.clearActiveUser()
        userDao.setActiveUser(user.id)
        return true
    }

    override suspend fun getActiveUserId(): Int? {
        return userDao.getActiveUserId()
    }

    override suspend fun logout() {
        userDao.clearActiveUser()
    }
}