package com.example.capstone2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone2.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()
    private val purchaseDao = db.purchaseDao()
    private val notificationDao = db.adminNotificationDao()
    private val invoiceDao = db.invoiceDao()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    val activeNotifications: Flow<List<AdminNotification>> = notificationDao.getActiveNotifications()
    val archivedNotifications: Flow<List<AdminNotification>> = notificationDao.getArchivedNotifications()
    val unreadCount: Flow<Int> = notificationDao.getUnreadCount()
    val actionRequiredNotifications: Flow<List<AdminNotification>> = notificationDao.getActionRequiredNotifications()

    val allInvoices: Flow<List<Invoice>> = invoiceDao.getAllInvoices()
    val flaggedInvoices: Flow<List<Invoice>> = invoiceDao.getFlaggedInvoices()

    init {
        viewModelScope.launch {
            val admin = userDao.getUserByEmail("admin@kdfgc.org")
            if (admin == null) {
                userDao.insertUser(
                    User(
                        email = "admin@kdfgc.org",
                        password = "admin123",
                        fullName = "KDFGC Admin",
                        memberNumber = "ADMIN001",
                        isAdmin = true
                    )
                )
            }

            val member = userDao.getUserByEmail("member@kdfgc.org")
            if (member == null) {
                userDao.insertUser(
                    User(
                        email = "member@kdfgc.org",
                        password = "member123",
                        fullName = "Demo Member",
                        memberNumber = "MEM001",
                        isAdmin = false
                    )
                )
            }

            val existingNotifications = notificationDao.getActiveNotifications().first()
            if (existingNotifications.isEmpty()) {
                addSampleNotifications()
            }
        }
    }

    private suspend fun addSampleNotifications() {
        val now = System.currentTimeMillis()

        val sampleMembers = listOf(
            User(email = "john@example.com", password = "pass123", fullName = "John Doe", memberNumber = "MEM002", isAdmin = false),
            User(email = "jane@example.com", password = "pass123", fullName = "Jane Smith", memberNumber = "MEM003", isAdmin = false),
            User(email = "mike@example.com", password = "pass123", fullName = "Mike Johnson", memberNumber = "MEM004", isAdmin = false),
            User(email = "sarah@example.com", password = "pass123", fullName = "Sarah Wilson", memberNumber = "MEM005", isAdmin = false),
            User(email = "tom@example.com", password = "pass123", fullName = "Tom Brown", memberNumber = "MEM006", isAdmin = false)
        )

        val userIds = mutableListOf<Int>()
        sampleMembers.forEach { user ->
            val existing = userDao.getUserByEmail(user.email)
            if (existing == null) {
                val id = userDao.insertUser(user)
                userIds.add(id.toInt())
            } else {
                userIds.add(existing.id)
            }
        }

        listOf(
            AdminNotification(
                type = "document",
                title = "Document Upload",
                message = "John Doe uploaded PAL License",
                timestamp = now - 2 * 60 * 1000,
                relatedUserId = userIds.getOrNull(0),
                actionRequired = true,
                actionType = "approve_doc"
            ),
            AdminNotification(
                type = "purchase",
                title = "New Purchase",
                message = "Jane Smith purchased 10-Visit Pass",
                timestamp = now - 15 * 60 * 1000,
                relatedUserId = userIds.getOrNull(1),
                relatedPurchaseId = 1
            ),
            AdminNotification(
                type = "member",
                title = "New Member",
                message = "Mike Johnson registered",
                timestamp = now - 60 * 60 * 1000,
                relatedUserId = userIds.getOrNull(2),
                actionRequired = true,
                actionType = "contact_member"
            ),
            AdminNotification(
                type = "document",
                title = "Document Upload",
                message = "Sarah Wilson uploaded RPAL Certificate",
                timestamp = now - 2 * 60 * 60 * 1000,
                relatedUserId = userIds.getOrNull(3),
                actionRequired = true,
                actionType = "approve_doc"
            ),
            AdminNotification(
                type = "alert",
                title = "Expiry Alert",
                message = "5 members have certifications expiring in 15 days",
                timestamp = now - 3 * 60 * 60 * 1000,
                actionRequired = true,
                actionType = "review_expiry"
            ),
            AdminNotification(
                type = "chargeback",
                title = "Payment Issue",
                message = "Chargeback received for Tom Brown's 5-Visit Pass",
                timestamp = now - 4 * 60 * 60 * 1000,
                relatedUserId = userIds.getOrNull(4),
                relatedPurchaseId = 2,
                actionRequired = true,
                actionType = "review_payment"
            )
        ).forEach { notificationDao.insertNotification(it) }
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.login(email, password)
        _currentUser.value = user
        return user != null
    }

    suspend fun register(email: String, password: String, fullName: String, memberNumber: String): Boolean {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return false

        val userId = userDao.insertUser(
            User(
                email = email,
                password = password,
                fullName = fullName,
                memberNumber = memberNumber,
                isAdmin = false
            )
        )
        _currentUser.value = userDao.getUserById(userId.toInt())

        notificationDao.insertNotification(
            AdminNotification(
                type = "member",
                title = "New Member",
                message = "$fullName registered",
                relatedUserId = userId.toInt(),
                actionRequired = true,
                actionType = "contact_member"
            )
        )

        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    fun searchMembers(name: String) {
        viewModelScope.launch {
            userDao.searchMembers(name).collect { users ->
                _searchResults.value = users
            }
        }
    }

    fun getAllMembers() {
        viewModelScope.launch {
            userDao.getAllMembers().collect { users ->
                _searchResults.value = users
            }
        }
    }

    fun getPurchasesForUser(userId: Int): Flow<List<Purchase>> {
        return purchaseDao.getActivePurchases(userId)
    }

    fun getInvoicesForUser(userId: Int): Flow<List<Invoice>> {
        return invoiceDao.getInvoicesForUser(userId)
    }

    fun addPurchase(userId: Int, itemName: String, quantity: Int, price: String, userName: String = "") {
        viewModelScope.launch {
            val purchaseId = purchaseDao.insertPurchase(
                Purchase(
                    userId = userId,
                    itemName = itemName,
                    totalQuantity = quantity,
                    remainingQuantity = quantity,
                    price = price
                )
            )

            val transactionId = "TXN${System.currentTimeMillis()}"
            invoiceDao.insertInvoice(
                Invoice(
                    purchaseId = purchaseId.toInt(),
                    userId = userId,
                    userName = userName,
                    itemName = itemName,
                    price = price,
                    quantity = quantity,
                    transactionId = transactionId
                )
            )

            notificationDao.insertNotification(
                AdminNotification(
                    type = "purchase",
                    title = "New Purchase",
                    message = "$userName purchased $itemName",
                    relatedUserId = userId,
                    relatedPurchaseId = purchaseId.toInt()
                )
            )
        }
    }

    fun deductPurchase(purchaseId: Int) {
        viewModelScope.launch {
            purchaseDao.deductOne(purchaseId)
        }
    }

    fun markNotificationAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationDao.markAsRead(notificationId)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            notificationDao.markAllAsRead()
        }
    }

    fun archiveNotification(notificationId: Int) {
        viewModelScope.launch {
            notificationDao.archiveNotification(notificationId)
        }
    }

    fun unarchiveNotification(notificationId: Int) {
        viewModelScope.launch {
            notificationDao.unarchiveNotification(notificationId)
        }
    }

    fun deleteNotification(notification: AdminNotification) {
        viewModelScope.launch {
            notificationDao.deleteNotification(notification)
        }
    }

    fun createNotification(
        type: String,
        title: String,
        message: String,
        relatedUserId: Int? = null,
        relatedPurchaseId: Int? = null,
        actionRequired: Boolean = false,
        actionType: String? = null
    ) {
        viewModelScope.launch {
            notificationDao.insertNotification(
                AdminNotification(
                    type = type,
                    title = title,
                    message = message,
                    relatedUserId = relatedUserId,
                    relatedPurchaseId = relatedPurchaseId,
                    actionRequired = actionRequired,
                    actionType = actionType
                )
            )
        }
    }

    fun getInvoiceForPurchase(purchaseId: Int, onResult: (Invoice?) -> Unit) {
        viewModelScope.launch {
            val invoice = invoiceDao.getInvoiceForPurchase(purchaseId)
            onResult(invoice)
        }
    }

    fun flagInvoice(invoiceId: Int, reason: String) {
        viewModelScope.launch {
            invoiceDao.flagInvoice(invoiceId, reason)

            notificationDao.insertNotification(
                AdminNotification(
                    type = "chargeback",
                    title = "Invoice Flagged",
                    message = "Invoice #$invoiceId flagged: $reason",
                    actionRequired = true,
                    actionType = "review_payment"
                )
            )
        }
    }

    fun unflagInvoice(invoiceId: Int) {
        viewModelScope.launch {
            invoiceDao.unflagInvoice(invoiceId)
        }
    }

    fun updateInvoiceStatus(invoiceId: Int, status: String) {
        viewModelScope.launch {
            invoiceDao.updatePaymentStatus(invoiceId, status)
        }
    }

    fun sendPaymentReminder(userId: Int, userName: String, invoiceId: Int) {
        viewModelScope.launch {
            notificationDao.insertNotification(
                AdminNotification(
                    type = "alert",
                    title = "Payment Reminder Sent",
                    message = "Payment reminder sent to $userName for Invoice #$invoiceId",
                    relatedUserId = userId
                )
            )
        }
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    fun sendAdminMessage(subject: String, body: String, targetUserIds: List<Int>) {
        viewModelScope.launch {
            if (targetUserIds.isEmpty()) {
                notificationDao.insertNotification(
                    AdminNotification(
                        type = "alert",
                        title = "Admin Message Sent",
                        message = "Broadcast: $subject",
                        actionRequired = false
                    )
                )
            } else {
                targetUserIds.forEach { userId ->
                    val user = userDao.getUserById(userId)
                    notificationDao.insertNotification(
                        AdminNotification(
                            type = "alert",
                            title = "Message Sent",
                            message = "Message sent to ${user?.fullName ?: "Member"}: $subject",
                            relatedUserId = userId,
                            actionRequired = false
                        )
                    )
                }
            }
        }
    }

    fun updateMemberStatus(userId: Int, status: String) {
        viewModelScope.launch {
            userDao.updateMembershipType(userId, status)
        }
    }

    fun updateMemberInfo(userId: Int, phone: String, palNumber: String) {
        viewModelScope.launch {
            userDao.updateMemberInfo(userId, phone, palNumber)
        }
    }
}
