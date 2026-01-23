package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminNotificationDao {
    @Query("SELECT * FROM admin_notifications WHERE isRead = 0 ORDER BY createdDate DESC")
    fun getUnreadNotifications(): Flow<List<AdminNotification>>

    @Query("SELECT * FROM admin_notifications ORDER BY createdDate DESC LIMIT 50")
    fun getAllNotifications(): Flow<List<AdminNotification>>

    @Query("SELECT COUNT(*) FROM admin_notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AdminNotification): Long

    @Query("UPDATE admin_notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Int)

    @Query("UPDATE admin_notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Delete
    suspend fun deleteNotification(notification: AdminNotification)
}