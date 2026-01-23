package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts WHERE parentId IS NULL ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<ForumPost>>

    @Query("SELECT * FROM forum_posts WHERE parentId IS NULL AND isHidden = 0 ORDER BY timestamp DESC")
    fun getVisiblePosts(): Flow<List<ForumPost>>

    @Query("SELECT * FROM forum_posts WHERE parentId IS NULL AND isHidden = 1 ORDER BY timestamp DESC")
    fun getHiddenPosts(): Flow<List<ForumPost>>

    @Query("SELECT * FROM forum_posts WHERE parentId IS NULL AND reportCount > 0 ORDER BY reportCount DESC")
    fun getFlaggedPosts(): Flow<List<ForumPost>>

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

    @Query("UPDATE forum_posts SET replyCount = replyCount + 1 WHERE id = :postId")
    suspend fun incrementReplyCount(postId: Int)

    @Query("UPDATE forum_posts SET isHidden = NOT isHidden WHERE id = :postId")
    suspend fun toggleHidePost(postId: Int)

    @Query("UPDATE forum_posts SET isHidden = 1 WHERE id = :postId")
    suspend fun hidePost(postId: Int)

    @Query("UPDATE forum_posts SET isHidden = 0 WHERE id = :postId")
    suspend fun unhidePost(postId: Int)

    @Query("UPDATE forum_posts SET reportCount = reportCount + 1 WHERE id = :postId")
    suspend fun reportPost(postId: Int)

    @Query("UPDATE forum_posts SET reportCount = 0 WHERE id = :postId")
    suspend fun clearReports(postId: Int)
}