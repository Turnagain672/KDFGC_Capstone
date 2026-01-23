package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_notifications")
data class AdminNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isArchived: Boolean = false,
    val relatedUserId: Int? = null,
    val relatedPurchaseId: Int? = null,
    val relatedDocumentId: Int? = null,
    val actionRequired: Boolean = false,
    val actionType: String? = null
)

@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val purchaseId: Int,
    val userId: Int,
    val userName: String,
    val itemName: String,
    val price: String,
    val quantity: Int,
    val purchaseDate: Long = System.currentTimeMillis(),
    val paymentMethod: String = "Card",
    val paymentStatus: String = "Paid",
    val transactionId: String = "",
    val notes: String = "",
    val isFlagged: Boolean = false,
    val flagReason: String = ""
)