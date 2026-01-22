package com.example.capstone2.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KdfgcRepository {
    fun getKdfgcData(): Flow<Result<KdfgcData>> = flow {
        try {
            val data = RetrofitClient.apiService.getKdfgcData()
            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}