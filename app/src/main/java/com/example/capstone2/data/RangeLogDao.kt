package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RangeLogDao {
    @Query("SELECT * FROM range_logs ORDER BY id DESC")
    fun getAllLogs(): Flow<List<RangeLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: RangeLog)

    @Delete
    suspend fun deleteLog(log: RangeLog)

    @Query("SELECT COUNT(*) FROM range_logs")
    fun getTotalVisits(): Flow<Int>

    @Query("SELECT SUM(rounds) FROM range_logs")
    fun getTotalRounds(): Flow<Int?>
}