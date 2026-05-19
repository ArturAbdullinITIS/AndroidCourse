package com.example.hw6.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.hw6.BuildConfig
import com.example.hw6.data.local.BookDao
import com.example.hw6.data.local.BookDatabase
import com.example.hw6.data.remote.api.BooksApiService
import com.example.hw6.data.repository.BookRepositoryImpl
import com.example.hw6.domain.repository.BookRepository
import com.example.hw6.util.ResourceProvider
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    @Singleton
    fun bindBookRepository(
        impl: BookRepositoryImpl
    ): BookRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): BookDatabase {
            return Room.databaseBuilder(
                klass = BookDatabase::class.java,
                context = context,
                name = "cached_books.db"
            ).build()
        }

        @Provides
        @Singleton
        fun provideBookDao(
            database: BookDatabase
        ): BookDao {
            return database.bookDao()
        }

        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url.newBuilder()
                        .addQueryParameter("key", BuildConfig.BOOKS_API_KEY)
                        .build()
                    val request = original.newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        }
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideBooksApiService(retrofit: Retrofit): BooksApiService {
            return retrofit.create(BooksApiService::class.java)
        }
        @Provides
        @Singleton
        fun provideResourceProvider(
            @ApplicationContext context: Context
        ): ResourceProvider {
            return ResourceProvider(context)
        }
    }
}