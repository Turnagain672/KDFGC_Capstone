package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_notifications")
data class AdminNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isArchived: Boolean = false,
    val relatedUserId: Int? = null,
    val relatedPurchaseId: Int? = null,
    val relatedDocumentId: Int? = null,
    val actionRequired: Boolean = false,
    val actionType: String? = null
)