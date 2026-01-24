package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE isPublished = 1 ORDER BY createdAt DESC")
    fun getPublishedNews(): Flow<List<News>>

    @Query("SELECT * FROM news WHERE isFeatured = 1 AND isPublished = 1 ORDER BY createdAt DESC LIMIT 5")
    fun getFeaturedNews(): Flow<List<News>>

    @Query("SELECT * FROM news ORDER BY createdAt DESC")
    fun getAllNews(): Flow<List<News>>

    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: Int): News?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News): Long

    @Update
    suspend fun updateNews(news: News)

    @Delete
    suspend fun deleteNews(news: News)

    @Query("UPDATE news SET isPublished = :published WHERE id = :newsId")
    suspend fun setPublished(newsId: Int, published: Boolean)

    @Query("UPDATE news SET isFeatured = :featured WHERE id = :newsId")
    suspend fun setFeatured(newsId: Int, featured: Boolean)
}