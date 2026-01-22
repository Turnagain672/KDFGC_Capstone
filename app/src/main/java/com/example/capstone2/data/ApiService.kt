package com.example.capstone2.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// Data classes matching JSON structure
data class KdfgcData(
    val courses: List<CourseData>,
    val events: List<EventData>,
    val clubInfo: ClubInfo
)

data class CourseData(
    val id: String,
    val name: String,
    val fullName: String,
    val description: String,
    val price: Int,
    val duration: String,
    val prerequisites: String,
    val minAge: String,
    val includes: String,
    val learnItems: List<String>,
    val dates: List<CourseDateData>
)

data class CourseDateData(
    val date: String,
    val time: String,
    val spotsLeft: Int
)

data class EventData(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
    val category: String
)

data class ClubInfo(
    val name: String,
    val address: String,
    val phone: String,
    val email: String,
    val rangeHours: String,
    val officeHours: String
)

// API Interface
interface KdfgcApiService {
    @GET("Turnagain672/KDFGC_Capstone/App/kdfgc-data.json")
    suspend fun getKdfgcData(): KdfgcData
}

// Retrofit instance
object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: KdfgcApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KdfgcApiService::class.java)
    }
}