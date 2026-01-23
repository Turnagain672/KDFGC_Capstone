package com.example.capstone2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "purchaseId")
    val purchaseId: Int = 0,

    @ColumnInfo(name = "userId")
    val userId: Int = 0,

    @ColumnInfo(name = "userName")
    val userName: String = "",

    @ColumnInfo(name = "itemName")
    val itemName: String = "",

    @ColumnInfo(name = "price")
    val price: String = "",

    @ColumnInfo(name = "quantity")
    val quantity: Int = 0,

    @ColumnInfo(name = "purchaseDate")
    val purchaseDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "paymentMethod")
    val paymentMethod: String = "Card",

    @ColumnInfo(name = "paymentStatus")
    val paymentStatus: String = "Paid",

    @ColumnInfo(name = "transactionId")
    val transactionId: String = "",

    @ColumnInfo(name = "notes")
    val notes: String = "",

    @ColumnInfo(name = "isFlagged")
    val isFlagged: Boolean = false,

    @ColumnInfo(name = "flagReason")
    val flagReason: String = ""
)