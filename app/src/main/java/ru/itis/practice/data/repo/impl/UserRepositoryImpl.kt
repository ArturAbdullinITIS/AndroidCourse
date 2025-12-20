package ru.itis.practice.data.repo.impl

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.itis.practice.data.background.DeleteUsersWorker
import ru.itis.practice.data.model.UserDbModel
import ru.itis.practice.data.repo.dao.UserDao
import ru.itis.practice.data.session.SessionDataStore
import ru.itis.practice.data.session.SessionManager
import ru.itis.practice.domain.repository.UserRepository
import ru.itis.practice.util.PasswordHasher
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionDataStore: SessionDataStore,
    private val workManager: WorkManager
): UserRepository {

    override suspend fun registerUser(email: String, password: String): Boolean {
        val existingUser = userDao.findUserByEmail(email)
        if (existingUser != null) {
            return false
        }
        val hashedPassword = PasswordHasher.hash(password)
        val userDbModel = UserDbModel(
            email = email,
            passwordHash = hashedPassword
        )

        val insertedId = userDao.registerUser(userDbModel)

        userDao.clearActiveUser()
        userDao.setActiveUser(insertedId.toInt())
        setSessionActive(true)
        return true
    }
    override suspend fun setSessionActive(isActive: Boolean) {
        sessionDataStore.setLoggedIn(isActive)
    }

    override suspend fun syncSessionWithDatabase() {
        val activeUserId = getActiveUserId()
        sessionDataStore.setLoggedIn(activeUserId != null)
    }

    override suspend fun softDeleteUser() {
        val activeUserId = userDao.getActiveUserId()
        userDao.softDeleteUser(activeUserId, System.currentTimeMillis())
    }

    override suspend fun restoreUser(userId: Int) {
        userDao.restoreUser(userId)
    }

    override suspend fun startDeleteOldUsersWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<DeleteUsersWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "Delete Old Users",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    override suspend fun deleteOldUsers() {
        val sevenDays = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000)
        userDao.deleteOldUsers(sevenDays)
    }

    override suspend fun findUserByEmail(email: String): UserDbModel? {
        return userDao.findUserByEmail(email)
    }

    override suspend fun findDeletedByEmail(email: String): UserDbModel? {
        return userDao.findDeletedByEmail(email)
    }

    override suspend fun hardDeleteUser(userId: Int) {
        userDao.hardDeleteUser(userId)
    }

    override suspend fun setActiveUser(userId: Int) {
        userDao.setActiveUser(userId)
    }




    override suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.findUserByEmail(email) ?: return false

        if (!PasswordHasher.verify(password, user.passwordHash)) {
            return false
        }

        userDao.clearActiveUser()
        userDao.setActiveUser(user.id)
        setSessionActive(true)
        return true
    }

    override suspend fun getActiveUserId(): Int? {
        return userDao.getActiveUserId()
    }

    override suspend fun logout() {
        userDao.clearActiveUser()
        sessionDataStore.setLoggedIn(false)
    }

    override suspend fun setUserName(userName: String) {
        val userId = getActiveUserId() ?: return
        userDao.setUserName(userId, userName)
    }

    override suspend fun getUserEmail(): String {
        val userId = getActiveUserId() ?: return ""
        return userDao.getUserEmail(userId)
    }

    override suspend fun getUsername(): String {
        val userId = getActiveUserId() ?: return ""
        return userDao.getUserName(userId)
    }
}