package com.example.capstone2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RangeLog::class, ForumPost::class, User::class, Purchase::class, Document::class, AdminNotification::class, Invoice::class, Course::class],
    version = 12,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rangeLogDao(): RangeLogDao
    abstract fun forumDao(): ForumDao
    abstract fun userDao(): UserDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun documentDao(): DocumentDao
    abstract fun adminNotificationDao(): AdminNotificationDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kdfgc_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}