package com.example.hw6.data.repository


import com.example.hw6.data.entity.CachedBooks
import com.example.hw6.data.local.BookDao
import com.example.hw6.data.mapper.toDomainModel
import com.example.hw6.data.remote.api.BooksApiService
import com.example.hw6.data.remote.dto.BookResponse
import com.example.hw6.domain.model.Book
import com.example.hw6.domain.repository.BookRepository
import com.example.hw6.util.DataSource
import com.example.hw6.util.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val booksApiService: BooksApiService,
    private val bookDao: BookDao,
    private val gson: Gson
): BookRepository{

    companion object {
        private const val DURATION = 30 * 1000
    }


    override suspend fun searchBooks(
        query: String,
        startIndex: Int,
        maxResults: Int
    ): Result<List<Book>> {
        val now = System.currentTimeMillis()
        return try {
            val cachedBooks = bookDao.getCachedBooks(query)

            if(cachedBooks != null && cachedBooks.expiry > now) {
                val books = gson.fromJson<List<BookResponse>>(
                    cachedBooks.responseJson,
                    object : TypeToken<List<BookResponse>>() {}.type
                ).map { it.toDomainModel() }
                Result.Success(books, DataSource.DATABASE)
            } else {
                val response = booksApiService.searchBooks(query, maxResults, startIndex)
                val booksJson = gson.toJson(response.items)
                val cachedBooks = CachedBooks(
                    query = query,
                    responseJson = booksJson,
                    timestamp = now,
                    expiry = now + DURATION
                )
                bookDao.saveBooksToCache(cachedBooks)
                bookDao.deleteExpiredCache(now)
                val books = response.items?.map { it.toDomainModel() } ?: emptyList()
                Result.Success(books, DataSource.API)
            }

        } catch (e: Exception) {
            Result.Failure(e)
        }

    }

    override suspend fun getBookDetails(bookId: String): Result<Book> {
        return try {
            val response = booksApiService.getBookDetails(bookId)
            val book = response.toDomainModel()
            Result.Success(book, DataSource.API)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

}