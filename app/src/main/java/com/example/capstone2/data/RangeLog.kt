package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "range_logs")
data class RangeLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val range: String,
    val firearm: String,
    val rounds: Int,
    val notes: String,
    val photoUri: String? = null
)