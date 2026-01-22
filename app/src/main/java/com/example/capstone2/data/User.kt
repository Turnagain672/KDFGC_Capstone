package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val fullName: String = "",
    val name: String = "",
    val memberNumber: String = "",
    val membershipType: String = "Pending",
    val isAdmin: Boolean = false,
    val phone: String = "",
    val palNumber: String = "",
    val registrationDate: Long = System.currentTimeMillis(),
    val isApproved: Boolean = false
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