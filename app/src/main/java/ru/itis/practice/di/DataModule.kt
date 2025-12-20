package ru.itis.practice.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.practice.data.image.ImageFileManager
import ru.itis.practice.data.session.SessionDataStore
import ru.itis.practice.data.repo.dao.MovieDao
import ru.itis.practice.data.repo.impl.MoviesRepositoryImpl
import ru.itis.practice.data.repo.RoomDatabase
import ru.itis.practice.data.repo.impl.UserRepositoryImpl
import ru.itis.practice.data.repo.dao.UserDao
import ru.itis.practice.domain.repository.MoviesRepository
import ru.itis.practice.domain.repository.UserRepository
import ru.itis.practice.util.ErrorParser
import ru.itis.practice.util.ResourceProvider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    fun bindMovieRepository(
        impl: MoviesRepositoryImpl
    ): MoviesRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): RoomDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = RoomDatabase::class.java,
                name = "movies.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext context: Context
        ): SessionDataStore {
            return SessionDataStore(context)
        }

        @Singleton
        @Provides
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager = WorkManager.getInstance(context)

        @Provides
        @Singleton
        fun provideErrorParser(
            resourceProvider: ResourceProvider
        ): ErrorParser {
            return ErrorParser(resourceProvider)
        }

        @Provides
        @Singleton
        fun provideMovieDao(
            database: RoomDatabase
        ): MovieDao {
            return database.movieDao()
        }

        @Provides
        @Singleton
        fun provideImageFileManager(
            @ApplicationContext context: Context
        ): ImageFileManager {
            return ImageFileManager(context)
        }

        @Provides
        @Singleton
        fun provideResourceProvider(
            @ApplicationContext context: Context
        ): ResourceProvider {
            return ResourceProvider(context)
        }

        @Provides
        @Singleton
        fun provideUserDao(
            database: RoomDatabase
        ): UserDao {
            return database.userDao()
        }
    }
}