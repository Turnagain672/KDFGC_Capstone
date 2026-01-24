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
    private val courseDao = db.courseDao()
    private val newsDao = db.newsDao()
    private val forumDao = db.forumDao()

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

    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()
    val activeCourses: Flow<List<Course>> = courseDao.getActiveCourses()
    val upcomingCourses: Flow<List<Course>> = courseDao.getUpcomingCoursesWithSeats()

    val allNews: Flow<List<News>> = newsDao.getAllNews()
    val featuredNews: Flow<List<News>> = newsDao.getFeaturedNews()
    val publishedNews: Flow<List<News>> = newsDao.getPublishedNews()

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val moderators: Flow<List<User>> = userDao.getModerators()
    val admins: Flow<List<User>> = userDao.getAdmins()

    val allForumPosts: Flow<List<ForumPost>> = forumDao.getAllPosts()

    init {
        viewModelScope.launch {
            val admin = userDao.getUserByEmail("admin@kdfgc.org")
            if (admin == null) {
                userDao.insertUser(User(email = "admin@kdfgc.org", password = "admin123", fullName = "KDFGC Admin", memberNumber = "ADMIN001", isAdmin = true, role = "admin"))
            }
            val member = userDao.getUserByEmail("member@kdfgc.org")
            if (member == null) {
                userDao.insertUser(User(email = "member@kdfgc.org", password = "member123", fullName = "Demo Member", memberNumber = "MEM001", isAdmin = false, role = "member"))
            }
            val existingNotifications = notificationDao.getActiveNotifications().first()
            if (existingNotifications.isEmpty()) { addSampleNotifications() }
            val existingCourses = courseDao.getAllCourses().first()
            if (existingCourses.isEmpty()) { addSampleCourses() }
            val existingInvoices = invoiceDao.getAllInvoices().first()
            if (existingInvoices.isEmpty()) { addSampleInvoices() }
            val existingNews = newsDao.getAllNews().first()
            if (existingNews.isEmpty()) { addSampleNews() }
        }
    }

    private suspend fun addSampleNews() {
        listOf(
            News(
                title = "Spring Shooting Season Opens!",
                summary = "The outdoor ranges are now open for the spring season. Check the schedule for availability.",
                content = "We're excited to announce that all outdoor ranges are now open for the spring shooting season!\n\nRange Hours:\n- Weekdays: 9 AM - 8 PM\n- Weekends: 8 AM - 6 PM\n\nPlease remember to follow all safety protocols and sign in at the clubhouse before using any range.\n\nSee you at the range!",
                authorName = "KDFGC Admin",
                isFeatured = true
            ),
            News(
                title = "New PAL Course Dates Available",
                summary = "Register now for upcoming PAL and RPAL courses in February and March.",
                content = "New course dates have been added for the Canadian Firearms Safety Course (CFSC) and Canadian Restricted Firearms Safety Course (CRFSC).\n\nUpcoming Dates:\n- Feb 15: CFSC (PAL)\n- Feb 22: CRFSC (RPAL)\n- Mar 8: CFSC (PAL)\n- Mar 15: CRFSC (RPAL)\n\nRegister through the app or contact the clubhouse. Space is limited!",
                authorName = "KDFGC Admin",
                isFeatured = true
            ),
            News(
                title = "Annual General Meeting - March 20",
                summary = "All members are invited to attend the AGM. Important club updates and board elections.",
                content = "The Annual General Meeting will be held on March 20, 2026 at 7:00 PM in the clubhouse.\n\nAgenda:\n- Financial report\n- Range improvements update\n- Board elections\n- Member Q&A\n\nAll members in good standing are encouraged to attend and vote.",
                authorName = "KDFGC Admin",
                isFeatured = false
            )
        ).forEach { newsDao.insertNews(it) }
    }

    private suspend fun addSampleInvoices() {
        val members = userDao.getAllMembersList()
        if (members.isNotEmpty()) {
            listOf(
                Invoice(userId = members[0].id, userName = members[0].fullName, itemName = "Annual Membership", price = "$250.00", quantity = 1, paymentStatus = "Paid", paymentMethod = "Credit Card", transactionId = "TXN${System.currentTimeMillis()}", notes = "2026 Annual Membership"),
                Invoice(userId = members[0].id, userName = members[0].fullName, itemName = "10-Visit Range Pass", price = "$75.00", quantity = 1, paymentStatus = "Paid", paymentMethod = "Credit Card", transactionId = "TXN${System.currentTimeMillis() + 1}", notes = ""),
                Invoice(userId = members.getOrElse(1) { members[0] }.id, userName = members.getOrElse(1) { members[0] }.fullName, itemName = "PAL Course Registration", price = "$150.00", quantity = 1, paymentStatus = "Pending", paymentMethod = "Pending", transactionId = "N/A", notes = "Awaiting payment"),
                Invoice(userId = members.getOrElse(2) { members[0] }.id, userName = members.getOrElse(2) { members[0] }.fullName, itemName = "Guest Pass", price = "$25.00", quantity = 2, paymentStatus = "Paid", paymentMethod = "E-Transfer", transactionId = "TXN${System.currentTimeMillis() + 2}", notes = "2 guest passes")
            ).forEach { invoiceDao.insertInvoice(it) }
        }
    }

    private suspend fun addSampleCourses() {
        listOf(
            Course(name = "PAL Course", description = "Canadian Firearms Safety Course for Possession and Acquisition License. Learn safe handling, storage, and transportation of non-restricted firearms.", date = "Feb 15, 2026", time = "9:00 AM - 5:00 PM", cost = "$150", classSize = 20, seatsRemaining = 12, instructorName = "John Smith", instructorEmail = "instructor@kdfgc.org", location = "KDFGC Clubhouse"),
            Course(name = "RPAL Course", description = "Restricted Firearms Safety Course for Restricted Possession and Acquisition License. Covers handguns and restricted rifles.", date = "Feb 22, 2026", time = "9:00 AM - 5:00 PM", cost = "$175", classSize = 15, seatsRemaining = 8, instructorName = "John Smith", instructorEmail = "instructor@kdfgc.org", location = "KDFGC Clubhouse"),
            Course(name = "Handgun Safety", description = "Advanced handgun safety and marksmanship course. Improve your shooting skills and safety practices.", date = "Mar 1, 2026", time = "10:00 AM - 3:00 PM", cost = "$100", classSize = 10, seatsRemaining = 6, instructorName = "Mike Johnson", instructorEmail = "mike@kdfgc.org", location = "KDFGC Pistol Range"),
            Course(name = "New Member Orientation", description = "Introduction to KDFGC facilities, rules, and range safety. Required for all new members.", date = "Weekly", time = "6:00 PM - 8:00 PM", cost = "Free", classSize = 30, seatsRemaining = 25, instructorName = "Club Staff", instructorEmail = "info@kdfgc.org", location = "KDFGC Clubhouse")
        ).forEach { courseDao.insertCourse(it) }
    }

    private suspend fun addSampleNotifications() {
        val now = System.currentTimeMillis()
        val sampleMembers = listOf(
            User(email = "john@example.com", password = "pass123", fullName = "John Doe", memberNumber = "MEM002", isAdmin = false, role = "member"),
            User(email = "jane@example.com", password = "pass123", fullName = "Jane Smith", memberNumber = "MEM003", isAdmin = false, role = "member"),
            User(email = "mike@example.com", password = "pass123", fullName = "Mike Johnson", memberNumber = "MEM004", isAdmin = false, role = "moderator"),
            User(email = "sarah@example.com", password = "pass123", fullName = "Sarah Wilson", memberNumber = "MEM005", isAdmin = false, role = "member"),
            User(email = "tom@example.com", password = "pass123", fullName = "Tom Brown", memberNumber = "MEM006", isAdmin = false, role = "member")
        )
        val userIds = mutableListOf<Int>()
        sampleMembers.forEach { user ->
            val existing = userDao.getUserByEmail(user.email)
            if (existing == null) { val id = userDao.insertUser(user); userIds.add(id.toInt()) } else { userIds.add(existing.id) }
        }
        listOf(
            AdminNotification(type = "document", title = "Document Upload", message = "John Doe uploaded PAL License", timestamp = now - 2 * 60 * 1000, relatedUserId = userIds.getOrNull(0), actionRequired = true, actionType = "approve_doc"),
            AdminNotification(type = "purchase", title = "New Purchase", message = "Jane Smith purchased 10-Visit Pass", timestamp = now - 15 * 60 * 1000, relatedUserId = userIds.getOrNull(1), relatedPurchaseId = 1),
            AdminNotification(type = "member", title = "New Member", message = "Mike Johnson registered", timestamp = now - 60 * 60 * 1000, relatedUserId = userIds.getOrNull(2), actionRequired = true, actionType = "contact_member"),
            AdminNotification(type = "document", title = "Document Upload", message = "Sarah Wilson uploaded RPAL Certificate", timestamp = now - 2 * 60 * 60 * 1000, relatedUserId = userIds.getOrNull(3), actionRequired = true, actionType = "approve_doc"),
            AdminNotification(type = "alert", title = "Expiry Alert", message = "5 members have certifications expiring in 15 days", timestamp = now - 3 * 60 * 60 * 1000, actionRequired = true, actionType = "review_expiry"),
            AdminNotification(type = "chargeback", title = "Payment Issue", message = "Chargeback received for Tom Brown's 5-Visit Pass", timestamp = now - 4 * 60 * 60 * 1000, relatedUserId = userIds.getOrNull(4), relatedPurchaseId = 2, actionRequired = true, actionType = "review_payment")
        ).forEach { notificationDao.insertNotification(it) }
    }

    suspend fun login(email: String, password: String): Boolean { val user = userDao.login(email, password); _currentUser.value = user; return user != null }

    suspend fun register(email: String, password: String, fullName: String, memberNumber: String): Boolean {
        val existing = userDao.getUserByEmail(email); if (existing != null) return false
        val userId = userDao.insertUser(User(email = email, password = password, fullName = fullName, memberNumber = memberNumber, isAdmin = false, role = "member"))
        _currentUser.value = userDao.getUserById(userId.toInt())
        notificationDao.insertNotification(AdminNotification(type = "member", title = "New Member", message = "$fullName registered", relatedUserId = userId.toInt(), actionRequired = true, actionType = "contact_member"))
        return true
    }

    fun logout() { _currentUser.value = null }
    fun searchMembers(name: String) { viewModelScope.launch { userDao.searchMembers(name).collect { _searchResults.value = it } } }
    fun getAllMembers() { viewModelScope.launch { userDao.getAllMembers().collect { _searchResults.value = it } } }
    fun getPurchasesForUser(userId: Int): Flow<List<Purchase>> = purchaseDao.getActivePurchases(userId)
    fun getInvoicesForUser(userId: Int): Flow<List<Invoice>> = invoiceDao.getInvoicesForUser(userId)

    fun addPurchase(userId: Int, itemName: String, quantity: Int, price: String, userName: String = "") {
        viewModelScope.launch {
            val purchaseId = purchaseDao.insertPurchase(Purchase(userId = userId, itemName = itemName, totalQuantity = quantity, remainingQuantity = quantity, price = price))
            val transactionId = "TXN${System.currentTimeMillis()}"
            invoiceDao.insertInvoice(Invoice(purchaseId = purchaseId.toInt(), userId = userId, userName = userName, itemName = itemName, price = price, quantity = quantity, transactionId = transactionId))
            notificationDao.insertNotification(AdminNotification(type = "purchase", title = "New Purchase", message = "$userName purchased $itemName", relatedUserId = userId, relatedPurchaseId = purchaseId.toInt()))
        }
    }

    fun deductPurchase(purchaseId: Int) { viewModelScope.launch { purchaseDao.deductOne(purchaseId) } }
    fun markNotificationAsRead(notificationId: Int) { viewModelScope.launch { notificationDao.markAsRead(notificationId) } }
    fun markAllNotificationsAsRead() { viewModelScope.launch { notificationDao.markAllAsRead() } }
    fun archiveNotification(notificationId: Int) { viewModelScope.launch { notificationDao.archiveNotification(notificationId) } }
    fun unarchiveNotification(notificationId: Int) { viewModelScope.launch { notificationDao.unarchiveNotification(notificationId) } }
    fun deleteNotification(notification: AdminNotification) { viewModelScope.launch { notificationDao.deleteNotification(notification) } }

    fun createNotification(type: String, title: String, message: String, relatedUserId: Int? = null, relatedPurchaseId: Int? = null, actionRequired: Boolean = false, actionType: String? = null) {
        viewModelScope.launch { notificationDao.insertNotification(AdminNotification(type = type, title = title, message = message, relatedUserId = relatedUserId, relatedPurchaseId = relatedPurchaseId, actionRequired = actionRequired, actionType = actionType)) }
    }

    fun getInvoiceForPurchase(purchaseId: Int, onResult: (Invoice?) -> Unit) { viewModelScope.launch { val invoice = invoiceDao.getInvoiceForPurchase(purchaseId); onResult(invoice) } }

    fun flagInvoice(invoiceId: Int, reason: String) {
        viewModelScope.launch {
            invoiceDao.flagInvoice(invoiceId, reason)
            notificationDao.insertNotification(AdminNotification(type = "chargeback", title = "Invoice Flagged", message = "Invoice #$invoiceId flagged: $reason", actionRequired = true, actionType = "review_payment"))
        }
    }

    fun unflagInvoice(invoiceId: Int) { viewModelScope.launch { invoiceDao.unflagInvoice(invoiceId) } }
    fun updateInvoiceStatus(invoiceId: Int, status: String) { viewModelScope.launch { invoiceDao.updatePaymentStatus(invoiceId, status) } }

    fun sendPaymentReminder(userId: Int, userName: String, invoiceId: Int) {
        viewModelScope.launch { notificationDao.insertNotification(AdminNotification(type = "alert", title = "Payment Reminder Sent", message = "Payment reminder sent to $userName for Invoice #$invoiceId", relatedUserId = userId)) }
    }

    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)

    fun sendAdminMessage(subject: String, body: String, targetUserIds: List<Int>) {
        viewModelScope.launch {
            if (targetUserIds.isEmpty()) { notificationDao.insertNotification(AdminNotification(type = "alert", title = "Admin Message Sent", message = "Broadcast: $subject", actionRequired = false)) }
            else { targetUserIds.forEach { userId -> val user = userDao.getUserById(userId); notificationDao.insertNotification(AdminNotification(type = "alert", title = "Message Sent", message = "Message sent to ${user?.fullName ?: "Member"}: $subject", relatedUserId = userId, actionRequired = false)) } }
        }
    }

    fun updateMemberStatus(userId: Int, status: String) { viewModelScope.launch { userDao.updateMembershipType(userId, status) } }
    fun updateMemberInfo(userId: Int, phone: String, palNumber: String) { viewModelScope.launch { userDao.updateMemberInfo(userId, phone, palNumber) } }

    fun sendExpiryReminders(subject: String, body: String) {
        viewModelScope.launch { notificationDao.insertNotification(AdminNotification(type = "alert", title = "Expiry Reminders Sent", message = "Reminder sent to members with expiring certifications: $subject", actionRequired = false)) }
    }

    fun addCourse(name: String, description: String, date: String, time: String, cost: String, classSize: Int, instructorName: String, instructorEmail: String, location: String, imageUri: String) {
        viewModelScope.launch {
            courseDao.insertCourse(Course(name = name, description = description, date = date, time = time, cost = cost, classSize = classSize, seatsRemaining = classSize, instructorName = instructorName, instructorEmail = instructorEmail, location = location, imageUri = imageUri))
            notificationDao.insertNotification(AdminNotification(type = "alert", title = "New Course Added", message = "Course '$name' scheduled for $date", actionRequired = false))
        }
    }

    fun updateCourse(course: Course) { viewModelScope.launch { courseDao.updateCourse(course) } }
    fun deleteCourse(course: Course) { viewModelScope.launch { courseDao.deleteCourse(course) } }

    fun registerForCourse(courseId: Int, userId: Int, userName: String, courseName: String) {
        viewModelScope.launch {
            courseDao.reserveSeat(courseId)
            notificationDao.insertNotification(AdminNotification(type = "member", title = "Course Registration", message = "$userName registered for $courseName", relatedUserId = userId, actionRequired = true, actionType = "contact_member"))
        }
    }

    fun cancelCourseRegistration(courseId: Int) { viewModelScope.launch { courseDao.cancelSeat(courseId) } }
    suspend fun getCourseById(courseId: Int): Course? = courseDao.getCourseById(courseId)

    // ==================== INVOICE MANAGEMENT ====================

    fun createCustomInvoice(userId: Int, userName: String, itemName: String, price: String, quantity: Int = 1, message: String = "") {
        viewModelScope.launch {
            val invoice = Invoice(userId = userId, userName = userName, itemName = itemName, price = price, quantity = quantity, paymentStatus = "Pending", paymentMethod = "Pending", transactionId = "INV-${System.currentTimeMillis()}", notes = message)
            invoiceDao.insertInvoice(invoice)
            notificationDao.insertNotification(AdminNotification(type = "invoice", title = "New Invoice: $itemName", message = "Amount: $price. $message", relatedUserId = userId, isAdminNotification = false))
            notificationDao.insertNotification(AdminNotification(type = "invoice", title = "Invoice Created", message = "Invoice for $itemName ($price) sent to $userName", relatedUserId = userId, isAdminNotification = true))
        }
    }

    fun processRefund(invoice: Invoice, refundReason: String) {
        viewModelScope.launch {
            invoiceDao.updatePaymentStatus(invoice.id, "Refunded")
            invoiceDao.updateInvoiceNotes(invoice.id, invoice.notes + "\nRefund reason: $refundReason")
            notificationDao.insertNotification(AdminNotification(type = "refund", title = "Refund Processed", message = "Your payment of ${invoice.price} for ${invoice.itemName} has been refunded. Reason: $refundReason", relatedUserId = invoice.userId, isAdminNotification = false))
            notificationDao.insertNotification(AdminNotification(type = "refund", title = "Refund Issued", message = "Refund of ${invoice.price} issued to ${invoice.userName} for ${invoice.itemName}", relatedUserId = invoice.userId, isAdminNotification = true))
        }
    }

    fun getMemberInvoices(userId: Int): Flow<List<Invoice>> = invoiceDao.getInvoicesForUser(userId)
    fun getMemberNotifications(userId: Int): Flow<List<AdminNotification>> = notificationDao.getMemberNotifications(userId)

    fun exportAllInvoices(invoices: List<Invoice>): String {
        var exportText = "KDFGC INVOICE EXPORT\n"
        exportText += "Generated: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}\n"
        exportText += "================================\n\n"
        invoices.forEach { inv ->
            exportText += "Invoice #${inv.id}\nCustomer: ${inv.userName}\nItem: ${inv.itemName}\nQuantity: ${inv.quantity}\nPrice: ${inv.price}\nStatus: ${inv.paymentStatus}\nTransaction ID: ${inv.transactionId}\nNotes: ${inv.notes}\n--------------------------------\n\n"
        }
        return exportText
    }

    fun updateInvoice(invoice: Invoice) { viewModelScope.launch { invoiceDao.updateInvoice(invoice) } }
    fun deleteInvoice(invoice: Invoice) { viewModelScope.launch { invoiceDao.deleteInvoice(invoice) } }

    fun sendInvoiceReminder(invoice: Invoice) {
        viewModelScope.launch {
            notificationDao.insertNotification(AdminNotification(type = "invoice", title = "Payment Reminder", message = "Reminder: Your invoice for ${invoice.itemName} (${invoice.price}) is pending payment.", relatedUserId = invoice.userId, isAdminNotification = false))
            notificationDao.insertNotification(AdminNotification(type = "alert", title = "Payment Reminder Sent", message = "Payment reminder sent to ${invoice.userName} for Invoice #${invoice.id}", relatedUserId = invoice.userId, isAdminNotification = true))
        }
    }

    // ==================== NEWS MANAGEMENT ====================

    fun createNews(title: String, summary: String, content: String, isFeatured: Boolean = true) {
        viewModelScope.launch {
            val authorName = _currentUser.value?.fullName ?: "Admin"
            val authorId = _currentUser.value?.id ?: 0
            newsDao.insertNews(News(title = title, summary = summary, content = content, authorId = authorId, authorName = authorName, isFeatured = isFeatured, isPublished = true))
            notificationDao.insertNotification(AdminNotification(type = "alert", title = "News Published", message = "\"$title\" has been published", isAdminNotification = true))
        }
    }

    fun updateNews(news: News) { viewModelScope.launch { newsDao.updateNews(news.copy(updatedAt = System.currentTimeMillis())) } }
    fun deleteNews(news: News) { viewModelScope.launch { newsDao.deleteNews(news) } }
    fun setNewsFeatured(newsId: Int, featured: Boolean) { viewModelScope.launch { newsDao.setFeatured(newsId, featured) } }
    fun setNewsPublished(newsId: Int, published: Boolean) { viewModelScope.launch { newsDao.setPublished(newsId, published) } }
    suspend fun getNewsById(newsId: Int): News? = newsDao.getNewsById(newsId)

    // ==================== USER MANAGEMENT ====================

    fun createUser(email: String, password: String, fullName: String, memberNumber: String, role: String, isAdmin: Boolean = false) {
        viewModelScope.launch {
            val existing = userDao.getUserByEmail(email)
            if (existing == null) {
                userDao.insertUser(User(email = email, password = password, fullName = fullName, memberNumber = memberNumber, role = role, isAdmin = isAdmin, membershipType = "Approved"))
                notificationDao.insertNotification(AdminNotification(type = "member", title = "User Created", message = "$fullName created as $role", isAdminNotification = true))
            }
        }
    }

    fun updateUserRole(userId: Int, role: String) {
        viewModelScope.launch {
            val isAdmin = role == "admin"
            userDao.setAdminStatus(userId, isAdmin, role)
            val user = userDao.getUserById(userId)
            notificationDao.insertNotification(AdminNotification(type = "member", title = "Role Updated", message = "${user?.fullName ?: "User"} is now $role", isAdminNotification = true))
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
            notificationDao.insertNotification(AdminNotification(type = "member", title = "User Deleted", message = "${user.fullName} has been removed", isAdminNotification = true))
        }
    }

    fun promoteToModerator(userId: Int) { updateUserRole(userId, "moderator") }
    fun promoteToAdmin(userId: Int) { updateUserRole(userId, "admin") }
    fun demoteToMember(userId: Int) { updateUserRole(userId, "member") }

    // ==================== FORUM MODERATION ====================

    fun deleteForumPost(post: ForumPost) {
        viewModelScope.launch {
            forumDao.deletePost(post)
            notificationDao.insertNotification(AdminNotification(type = "alert", title = "Post Deleted", message = "Forum post by ${post.authorName} was removed", isAdminNotification = true))
        }
    }

    fun getForumPostsByUser(userId: Int): Flow<List<ForumPost>> = forumDao.getPostsByUser(userId)
}