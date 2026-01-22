package com.example.capstone2.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("DataSyncWorker", "Starting background data sync...")

            val data = RetrofitClient.apiService.getKdfgcData()

            Log.d("DataSyncWorker", "Sync complete! Loaded ${data.courses.size} courses, ${data.events.size} events")

            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Sync failed: ${e.message}")
            Result.retry()
        }
    }
}