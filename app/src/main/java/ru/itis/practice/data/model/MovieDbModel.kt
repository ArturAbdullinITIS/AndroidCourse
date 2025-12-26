package ru.itis.practice.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id")]
)
data class MovieDbModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="user_id")
    val userId: Int?,
    @ColumnInfo(name="title")
    val title: String,
    @ColumnInfo(name="description")
    val description: String,
    @ColumnInfo(name="release_year")
    val releaseYear: Int,
    @ColumnInfo(name="rating")
    val rating: Double
)