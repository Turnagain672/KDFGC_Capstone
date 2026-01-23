package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminNotificationDao {
    @Query("SELECT * FROM admin_notifications WHERE isArchived = 0 ORDER BY timestamp DESC")
    fun getActiveNotifications(): Flow<List<AdminNotification>>

    @Query("SELECT * FROM admin_notifications WHERE isArchived = 1 ORDER BY timestamp DESC")
    fun getArchivedNotifications(): Flow<List<AdminNotification>>

    @Query("SELECT COUNT(*) FROM admin_notifications WHERE isRead = 0 AND isArchived = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT * FROM admin_notifications WHERE actionRequired = 1 AND isArchived = 0 ORDER BY timestamp DESC")
    fun getActionRequiredNotifications(): Flow<List<AdminNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AdminNotification): Long

    @Update
    suspend fun updateNotification(notification: AdminNotification)

    @Query("UPDATE admin_notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Int)

    @Query("UPDATE admin_notifications SET isRead = 1 WHERE isArchived = 0")
    suspend fun markAllAsRead()

    @Query("UPDATE admin_notifications SET isArchived = 1 WHERE id = :notificationId")
    suspend fun archiveNotification(notificationId: Int)

    @Query("UPDATE admin_notifications SET isArchived = 0 WHERE id = :notificationId")
    suspend fun unarchiveNotification(notificationId: Int)

    @Delete
    suspend fun deleteNotification(notification: AdminNotification)
}

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY purchaseDate DESC")
    fun getAllInvoices(): Flow<List<Invoice>>

    @Query("SELECT * FROM invoices WHERE userId = :userId ORDER BY purchaseDate DESC")
    fun getInvoicesForUser(userId: Int): Flow<List<Invoice>>

    @Query("SELECT * FROM invoices WHERE purchaseId = :purchaseId LIMIT 1")
    suspend fun getInvoiceForPurchase(purchaseId: Int): Invoice?

    @Query("SELECT * FROM invoices WHERE isFlagged = 1 ORDER BY purchaseDate DESC")
    fun getFlaggedInvoices(): Flow<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: Invoice): Long

    @Update
    suspend fun updateInvoice(invoice: Invoice)

    @Query("UPDATE invoices SET isFlagged = 1, flagReason = :reason WHERE id = :invoiceId")
    suspend fun flagInvoice(invoiceId: Int, reason: String)

    @Query("UPDATE invoices SET isFlagged = 0, flagReason = '' WHERE id = :invoiceId")
    suspend fun unflagInvoice(invoiceId: Int)

    @Query("UPDATE invoices SET paymentStatus = :status WHERE id = :invoiceId")
    suspend fun updatePaymentStatus(invoiceId: Int, status: String)
}