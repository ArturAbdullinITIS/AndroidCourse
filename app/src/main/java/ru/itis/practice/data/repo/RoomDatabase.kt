package ru.itis.practice.data.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.practice.data.model.MovieDbModel
import ru.itis.practice.data.model.UserDbModel
import ru.itis.practice.data.repo.dao.MovieDao
import ru.itis.practice.data.repo.dao.UserDao


@Database(
    entities = [UserDbModel::class, MovieDbModel::class],
    version = 5,
    exportSchema = false
)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao

}