package com.example.capstone2.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ForumViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).forumDao()

    val allPosts: Flow<List<ForumPost>> = dao.getAllPosts()

    fun getReplies(postId: Int): Flow<List<ForumPost>> = dao.getReplies(postId)

    fun getReplyCount(postId: Int): Flow<Int> = dao.getReplyCount(postId)

    fun addPost(author: String, content: String, category: String = "general", photoUri: String? = null) {
        viewModelScope.launch {
            dao.insertPost(
                ForumPost(
                    author = author,
                    content = content,
                    category = category,
                    photoUri = photoUri
                )
            )
        }
    }

    fun addReply(author: String, content: String, parentId: Int) {
        viewModelScope.launch {
            dao.insertPost(
                ForumPost(
                    author = author,
                    content = content,
                    parentId = parentId
                )
            )
            dao.incrementReplyCount(parentId)
        }
    }

    fun deletePost(post: ForumPost) {
        viewModelScope.launch {
            dao.deletePost(post)
        }
    }

    fun likePost(postId: Int) {
        viewModelScope.launch {
            dao.likePost(postId)
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            dao.unlikePost(postId)
        }
    }
}