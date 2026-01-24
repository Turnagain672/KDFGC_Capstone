package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val cost: String = "$0",
    val classSize: Int = 20,
    val seatsRemaining: Int = 20,
    val imageUri: String = "",
    val instructorName: String = "",
    val instructorEmail: String = "",
    val location: String = "KDFGC Range",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)