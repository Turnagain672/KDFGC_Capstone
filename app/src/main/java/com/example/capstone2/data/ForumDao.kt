package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts WHERE parentId IS NULL ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<ForumPost>>

    @Query("SELECT * FROM forum_posts WHERE parentId = :postId ORDER BY timestamp ASC")
    fun getReplies(postId: Int): Flow<List<ForumPost>>

    @Query("SELECT COUNT(*) FROM forum_posts WHERE parentId = :postId")
    fun getReplyCount(postId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPost)

    @Delete
    suspend fun deletePost(post: ForumPost)

    @Query("UPDATE forum_posts SET likes = likes + 1 WHERE id = :postId")
    suspend fun likePost(postId: Int)

    @Query("UPDATE forum_posts SET likes = likes - 1 WHERE id = :postId")
    suspend fun unlikePost(postId: Int)
}