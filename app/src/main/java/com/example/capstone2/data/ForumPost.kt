package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forum_posts")
data class ForumPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val parentId: Int? = null,
    val category: String = "general",
    val replyCount: Int = 0,
    val photoUri: String? = null
)