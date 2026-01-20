package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val fullName: String,
    val memberNumber: String,
    val isAdmin: Boolean = false
)

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val itemName: String,
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val price: String,
    val purchaseDate: Long = System.currentTimeMillis()
)