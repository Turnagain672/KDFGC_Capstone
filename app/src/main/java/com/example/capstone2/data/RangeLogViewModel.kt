package com.example.capstone2.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RangeLogViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).rangeLogDao()

    val allLogs: Flow<List<RangeLog>> = dao.getAllLogs()
    val totalVisits: Flow<Int> = dao.getTotalVisits()
    val totalRounds: Flow<Int> = dao.getTotalRounds().map { it ?: 0 }

    fun addLog(date: String, range: String, firearm: String, rounds: Int, notes: String, photoUri: String? = null) {
        viewModelScope.launch {
            dao.insertLog(
                RangeLog(
                    date = date,
                    range = range,
                    firearm = firearm,
                    rounds = rounds,
                    notes = notes,
                    photoUri = photoUri
                )
            )
        }
    }

    fun deleteLog(log: RangeLog) {
        viewModelScope.launch {
            dao.deleteLog(log)
        }
    }
}