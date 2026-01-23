package com.example.capstone2.ui.screens

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.AdminNotification
import com.example.capstone2.data.Invoice
import com.example.capstone2.data.Purchase
import com.example.capstone2.data.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminPanelScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentUser by userViewModel.currentUser.collectAsState()
    val unreadCount by userViewModel.unreadCount.collectAsState(initial = 0)

    LaunchedEffect(currentUser) {
        if (currentUser == null || currentUser?.isAdmin != true) {
            navController.navigate("login") {
                popUpTo("adminpanel") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF8B0000), Color(0xFFB22222))
                    )
                )
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("ADMIN PANEL", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Club Management", color = Color(0xFFFFCCCC), fontSize = 12.sp)
            }
            IconButton(
                onClick = {
                    userViewModel.logout()
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            }
        }

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF252525),
            contentColor = Color.White,
            edgePadding = 8.dp
        ) {
            AdminTab(0, selectedTab, "Members", Icons.Default.People) { selectedTab = 0 }
            AdminTabWithBadge(1, selectedTab, "Notifications", Icons.Default.Notifications, unreadCount) { selectedTab = 1 }
            AdminTab(2, selectedTab, "Archive", Icons.Default.Archive) { selectedTab = 2 }
            AdminTab(3, selectedTab, "Invoices", Icons.Default.Receipt) { selectedTab = 3 }
            AdminTab(4, selectedTab, "Documents", Icons.Default.Description) { selectedTab = 4 }
            AdminTab(5, selectedTab, "Forum", Icons.Default.Forum) { selectedTab = 5 }
            AdminTab(6, selectedTab, "Courses", Icons.Default.School) { selectedTab = 6 }
            AdminTab(7, selectedTab, "Messages", Icons.Default.Email) { selectedTab = 7 }
        }

        // Content
        when (selectedTab) {
            0 -> MembersTab(userViewModel)
            1 -> NotificationsTab(userViewModel, onNavigateToMember = { userId ->
                // Handle navigation to member
            })
            2 -> ArchiveTab(userViewModel)
            3 -> InvoicesTab(userViewModel)
            4 -> DocumentsTab(userViewModel)
            5 -> ForumModerationTab(userViewModel)
            6 -> CoursesTab(userViewModel)
            7 -> MessagesTab(userViewModel)
        }
    }
}

@Composable
fun AdminTab(index: Int, selectedTab: Int, title: String, icon: ImageVector, onClick: () -> Unit) {
    Tab(
        selected = selectedTab == index,
        onClick = onClick,
        text = { Text(title, fontSize = 12.sp) },
        icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
        selectedContentColor = Color(0xFFFF6B6B),
        unselectedContentColor = Color(0xFF888888)
    )
}

@Composable
fun AdminTabWithBadge(index: Int, selectedTab: Int, title: String, icon: ImageVector, badgeCount: Int, onClick: () -> Unit) {
    Tab(
        selected = selectedTab == index,
        onClick = onClick,
        text = { Text(title, fontSize = 12.sp) },
        icon = {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(containerColor = Color(0xFFFF5252)) {
                            Text(badgeCount.toString(), fontSize = 10.sp)
                        }
                    }
                }
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        },
        selectedContentColor = Color(0xFFFF6B6B),
        unselectedContentColor = Color(0xFF888888)
    )
}

// Helper function to format timestamp
fun formatTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val minutes = diff / 60000
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "${days}d ago"
        hours > 0 -> "${hours}h ago"
        minutes > 0 -> "${minutes}m ago"
        else -> "Just now"
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
// ==================== MEMBERS TAB ====================
@Composable
fun MembersTab(userViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMember by remember { mutableStateOf<User?>(null) }
    val searchResults by userViewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getAllMembers()
    }

    if (selectedMember != null) {
        MemberDetailView(
            member = selectedMember!!,
            userViewModel = userViewModel,
            onBack = { selectedMember = null }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isNotBlank()) {
                        userViewModel.searchMembers(it)
                    } else {
                        userViewModel.getAllMembers()
                    }
                },
                label = { Text("Search member by name") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF888888)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB22222),
                    unfocusedBorderColor = Color(0xFF444444),
                    focusedLabelColor = Color(0xFFFF6B6B),
                    unfocusedLabelColor = Color(0xFF888888),
                    cursorColor = Color(0xFFFF6B6B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("MEMBERS (${searchResults.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            if (searchResults.isEmpty()) {
                EmptyStateCard("👥", "No Members Found", "Members will appear here after registration")
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    searchResults.forEach { member ->
                        MemberCard(member = member, onClick = { selectedMember = member })
                    }
                }
            }
        }
    }
}

// ==================== NOTIFICATIONS TAB ====================
@Composable
fun NotificationsTab(userViewModel: UserViewModel, onNavigateToMember: (Int) -> Unit) {
    val notifications by userViewModel.activeNotifications.collectAsState(initial = emptyList())
    var selectedNotification by remember { mutableStateOf<AdminNotification?>(null) }
    var showMemberDetail by remember { mutableStateOf<User?>(null) }
    var showInvoiceDetail by remember { mutableStateOf<Invoice?>(null) }

    if (showMemberDetail != null) {
        MemberDetailView(
            member = showMemberDetail!!,
            userViewModel = userViewModel,
            onBack = { showMemberDetail = null }
        )
        return
    }

    if (showInvoiceDetail != null) {
        InvoiceDetailView(
            invoice = showInvoiceDetail!!,
            userViewModel = userViewModel,
            onBack = { showInvoiceDetail = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("RECENT NOTIFICATIONS (${notifications.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { userViewModel.markAllNotificationsAsRead() }) {
                Text("Mark All Read", color = Color(0xFF888888), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (notifications.isEmpty()) {
            EmptyStateCard("🔔", "No Notifications", "New activity will appear here")
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                notifications.forEach { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = {
                            userViewModel.markNotificationAsRead(notification.id)
                            selectedNotification = notification
                        },
                        onArchive = {
                            userViewModel.archiveNotification(notification.id)
                        },
                        onViewMember = { userId ->
                            viewModelScope(userViewModel, userId) { user ->
                                showMemberDetail = user
                            }
                        },
                        onViewInvoice = { purchaseId ->
                            userViewModel.getInvoiceForPurchase(purchaseId) { invoice ->
                                showInvoiceDetail = invoice
                            }
                        }
                    )
                }
            }
        }
    }

    // Notification Detail Dialog
    selectedNotification?.let { notification ->
        NotificationDetailDialog(
            notification = notification,
            userViewModel = userViewModel,
            onDismiss = { selectedNotification = null },
            onViewMember = { userId ->
                selectedNotification = null
                viewModelScope(userViewModel, userId) { user ->
                    showMemberDetail = user
                }
            },
            onViewInvoice = { purchaseId ->
                selectedNotification = null
                userViewModel.getInvoiceForPurchase(purchaseId) { invoice ->
                    showInvoiceDetail = invoice
                }
            }
        )
    }
}

// Helper to get user
fun viewModelScope(userViewModel: UserViewModel, userId: Int, onResult: (User?) -> Unit) {
    kotlinx.coroutines.GlobalScope.launch {
        val user = userViewModel.getUserById(userId)
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
            onResult(user)
        }
    }
}

@Composable
fun NotificationCard(
    notification: AdminNotification,
    onClick: () -> Unit,
    onArchive: () -> Unit,
    onViewMember: (Int) -> Unit,
    onViewInvoice: (Int) -> Unit
) {
    val icon = when (notification.type) {
        "document" -> Icons.Default.Description
        "purchase" -> Icons.Default.ShoppingCart
        "member" -> Icons.Default.PersonAdd
        "alert" -> Icons.Default.Warning
        "chargeback" -> Icons.Default.CreditCardOff
        "expiry" -> Icons.Default.Schedule
        else -> Icons.Default.Notifications
    }
    val iconColor = when (notification.type) {
        "document" -> Color(0xFF2196F3)
        "purchase" -> Color(0xFF4CAF50)
        "member" -> Color(0xFF9C27B0)
        "alert" -> Color(0xFFFF9800)
        "chargeback" -> Color(0xFFFF5252)
        "expiry" -> Color(0xFFFFEB3B)
        else -> Color(0xFF888888)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color(0xFF252525) else Color(0xFF2D2D2D)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Unread indicator
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFFF6B6B), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(notification.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        if (notification.actionRequired) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFF5252).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    "ACTION",
                                    color = Color(0xFFFF5252),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(notification.message, color = Color(0xFF888888), fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Text(formatTimeAgo(notification.timestamp), color = Color(0xFF666666), fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // View related item button
                if (notification.relatedUserId != null) {
                    OutlinedButton(
                        onClick = { onViewMember(notification.relatedUserId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("VIEW MEMBER", fontSize = 10.sp)
                    }
                }

                if (notification.relatedPurchaseId != null) {
                    OutlinedButton(
                        onClick = { onViewInvoice(notification.relatedPurchaseId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Receipt, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("VIEW INVOICE", fontSize = 10.sp)
                    }
                }

                OutlinedButton(
                    onClick = onArchive,
                    modifier = if (notification.relatedUserId == null && notification.relatedPurchaseId == null) Modifier.fillMaxWidth() else Modifier,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF888888)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Archive, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ARCHIVE", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun NotificationDetailDialog(
    notification: AdminNotification,
    userViewModel: UserViewModel,
    onDismiss: () -> Unit,
    onViewMember: (Int) -> Unit,
    onViewInvoice: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = when (notification.type) {
                    "document" -> Icons.Default.Description
                    "purchase" -> Icons.Default.ShoppingCart
                    "member" -> Icons.Default.PersonAdd
                    "alert" -> Icons.Default.Warning
                    "chargeback" -> Icons.Default.CreditCardOff
                    else -> Icons.Default.Notifications
                }
                Icon(icon, null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(notification.title, color = Color.White)
            }
        },
        text = {
            Column {
                Text(notification.message, color = Color(0xFFCCCCCC), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Time: ${formatDate(notification.timestamp)}", color = Color(0xFF888888), fontSize = 12.sp)

                if (notification.actionRequired) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFF5252).copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when (notification.actionType) {
                                    "approve_doc" -> "Document needs approval"
                                    "review_payment" -> "Payment issue needs review"
                                    "contact_member" -> "New member needs welcome contact"
                                    "review_expiry" -> "Review expiring certifications"
                                    else -> "Action required"
                                },
                                color = Color(0xFFFF5252),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (notification.relatedUserId != null) {
                    Button(
                        onClick = { onViewMember(notification.relatedUserId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("VIEW MEMBER")
                    }
                }
                if (notification.relatedPurchaseId != null) {
                    Button(
                        onClick = { onViewInvoice(notification.relatedPurchaseId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("VIEW INVOICE")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                userViewModel.archiveNotification(notification.id)
                onDismiss()
            }) {
                Text("ARCHIVE", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}

// ==================== ARCHIVE TAB ====================
@Composable
fun ArchiveTab(userViewModel: UserViewModel) {
    val archivedNotifications by userViewModel.archivedNotifications.collectAsState(initial = emptyList())
    var selectedNotification by remember { mutableStateOf<AdminNotification?>(null) }
    var showMemberDetail by remember { mutableStateOf<User?>(null) }
    var showInvoiceDetail by remember { mutableStateOf<Invoice?>(null) }

    if (showMemberDetail != null) {
        MemberDetailView(
            member = showMemberDetail!!,
            userViewModel = userViewModel,
            onBack = { showMemberDetail = null }
        )
        return
    }

    if (showInvoiceDetail != null) {
        InvoiceDetailView(
            invoice = showInvoiceDetail!!,
            userViewModel = userViewModel,
            onBack = { showInvoiceDetail = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ARCHIVED ITEMS (${archivedNotifications.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Historical records", color = Color(0xFF888888), fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (archivedNotifications.isEmpty()) {
            EmptyStateCard("📦", "Archive Empty", "Archived notifications will appear here for future reference")
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                archivedNotifications.forEach { notification ->
                    ArchivedNotificationCard(
                        notification = notification,
                        onClick = { selectedNotification = notification },
                        onUnarchive = { userViewModel.unarchiveNotification(notification.id) },
                        onDelete = { userViewModel.deleteNotification(notification) },
                        onViewMember = { userId ->
                            viewModelScope(userViewModel, userId) { user ->
                                showMemberDetail = user
                            }
                        },
                        onViewInvoice = { purchaseId ->
                            userViewModel.getInvoiceForPurchase(purchaseId) { invoice ->
                                showInvoiceDetail = invoice
                            }
                        }
                    )
                }
            }
        }
    }

    selectedNotification?.let { notification ->
        ArchivedDetailDialog(
            notification = notification,
            userViewModel = userViewModel,
            onDismiss = { selectedNotification = null },
            onViewMember = { userId ->
                selectedNotification = null
                viewModelScope(userViewModel, userId) { user ->
                    showMemberDetail = user
                }
            },
            onViewInvoice = { purchaseId ->
                selectedNotification = null
                userViewModel.getInvoiceForPurchase(purchaseId) { invoice ->
                    showInvoiceDetail = invoice
                }
            }
        )
    }
}

@Composable
fun ArchivedNotificationCard(
    notification: AdminNotification,
    onClick: () -> Unit,
    onUnarchive: () -> Unit,
    onDelete: () -> Unit,
    onViewMember: (Int) -> Unit,
    onViewInvoice: (Int) -> Unit
) {
    val icon = when (notification.type) {
        "document" -> Icons.Default.Description
        "purchase" -> Icons.Default.ShoppingCart
        "member" -> Icons.Default.PersonAdd
        "alert" -> Icons.Default.Warning
        "chargeback" -> Icons.Default.CreditCardOff
        "expiry" -> Icons.Default.Schedule
        else -> Icons.Default.Notifications
    }
    val iconColor = when (notification.type) {
        "document" -> Color(0xFF2196F3)
        "purchase" -> Color(0xFF4CAF50)
        "member" -> Color(0xFF9C27B0)
        "alert" -> Color(0xFFFF9800)
        "chargeback" -> Color(0xFFFF5252)
        "expiry" -> Color(0xFFFFEB3B)
        else -> Color(0xFF888888)
    }.copy(alpha = 0.6f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Archive, contentDescription = null, tint = Color(0xFF666666), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(notification.title, color = Color(0xFFAAAAAA), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(notification.message, color = Color(0xFF666666), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(formatDate(notification.timestamp), color = Color(0xFF555555), fontSize = 9.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (notification.relatedUserId != null) {
                    OutlinedButton(
                        onClick = { onViewMember(notification.relatedUserId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3).copy(alpha = 0.7f)),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("MEMBER", fontSize = 9.sp)
                    }
                }

                if (notification.relatedPurchaseId != null) {
                    OutlinedButton(
                        onClick = { onViewInvoice(notification.relatedPurchaseId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50).copy(alpha = 0.7f)),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Receipt, null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("INVOICE", fontSize = 9.sp)
                    }
                }

                OutlinedButton(
                    onClick = onUnarchive,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF888888)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Unarchive, null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RESTORE", fontSize = 9.sp)
                }

                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252).copy(alpha = 0.7f)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
fun ArchivedDetailDialog(
    notification: AdminNotification,
    userViewModel: UserViewModel,
    onDismiss: () -> Unit,
    onViewMember: (Int) -> Unit,
    onViewInvoice: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Archive, null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Archived: ${notification.title}", color = Color.White, fontSize = 16.sp)
            }
        },
        text = {
            Column {
                Text(notification.message, color = Color(0xFFCCCCCC), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Original Date: ${formatDate(notification.timestamp)}", color = Color(0xFF888888), fontSize = 12.sp)
                Text("Type: ${notification.type.replaceFirstChar { it.uppercase() }}", color = Color(0xFF888888), fontSize = 12.sp)

                if (notification.actionType != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Original Action: ${notification.actionType.replace("_", " ").replaceFirstChar { it.uppercase() }}",
                        color = Color(0xFF666666),
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (notification.relatedUserId != null) {
                    Button(
                        onClick = { onViewMember(notification.relatedUserId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("VIEW MEMBER", fontSize = 12.sp)
                    }
                }
                if (notification.relatedPurchaseId != null) {
                    Button(
                        onClick = { onViewInvoice(notification.relatedPurchaseId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("VIEW INVOICE", fontSize = 12.sp)
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}

// ==================== INVOICES TAB ====================
@Composable
fun InvoicesTab(userViewModel: UserViewModel) {
    val allInvoices by userViewModel.allInvoices.collectAsState(initial = emptyList())
    val flaggedInvoices by userViewModel.flaggedInvoices.collectAsState(initial = emptyList())
    var selectedFilter by remember { mutableStateOf("all") }
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }

    val displayedInvoices = when (selectedFilter) {
        "flagged" -> flaggedInvoices
        "paid" -> allInvoices.filter { it.paymentStatus == "Paid" }
        "pending" -> allInvoices.filter { it.paymentStatus == "Pending" || it.paymentStatus == "Failed" }
        "chargeback" -> allInvoices.filter { it.paymentStatus == "Chargeback" }
        else -> allInvoices
    }

    if (selectedInvoice != null) {
        InvoiceDetailView(
            invoice = selectedInvoice!!,
            userViewModel = userViewModel,
            onBack = { selectedInvoice = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("INVOICES & PAYMENTS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text("All (${allInvoices.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFB22222),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFF333333),
                    labelColor = Color(0xFF888888)
                )
            )
            FilterChip(
                selected = selectedFilter == "flagged",
                onClick = { selectedFilter = "flagged" },
                label = { Text("Flagged (${flaggedInvoices.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF5252),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFF333333),
                    labelColor = Color(0xFF888888)
                )
            )
            FilterChip(
                selected = selectedFilter == "pending",
                onClick = { selectedFilter = "pending" },
                label = { Text("Issues", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF9800),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFF333333),
                    labelColor = Color(0xFF888888)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (displayedInvoices.isEmpty()) {
            EmptyStateCard("🧾", "No Invoices", "Purchase invoices will appear here")
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                displayedInvoices.forEach { invoice ->
                    InvoiceCard(
                        invoice = invoice,
                        onClick = { selectedInvoice = invoice }
                    )
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(invoice: Invoice, onClick: () -> Unit) {
    val statusColor = when (invoice.paymentStatus) {
        "Paid" -> Color(0xFF4CAF50)
        "Pending" -> Color(0xFFFF9800)
        "Failed" -> Color(0xFFFF5252)
        "Chargeback" -> Color(0xFFFF5252)
        "Refunded" -> Color(0xFF2196F3)
        else -> Color(0xFF888888)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (invoice.isFlagged) Color(0xFF3D1F1F) else Color(0xFF252525)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (invoice.isFlagged) {
                    Icon(Icons.Default.Flag, null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Invoice #${invoice.id}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(invoice.userName, color = Color(0xFF888888), fontSize = 12.sp)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        invoice.paymentStatus,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(invoice.itemName, color = Color(0xFFCCCCCC), fontSize = 13.sp)
                Text(invoice.price, color = Color(0xFF90EE90), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Qty: ${invoice.quantity}", color = Color(0xFF666666), fontSize = 11.sp)
                Text(formatDate(invoice.purchaseDate), color = Color(0xFF666666), fontSize = 11.sp)
            }

            if (invoice.isFlagged && invoice.flagReason.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFF5252).copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, null, tint = Color(0xFFFF5252), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(invoice.flagReason, color = Color(0xFFFF5252), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceDetailView(
    invoice: Invoice,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    var showFlagDialog by remember { mutableStateOf(false) }
    var showSendReminderDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }

    val statusColor = when (invoice.paymentStatus) {
        "Paid" -> Color(0xFF4CAF50)
        "Pending" -> Color(0xFFFF9800)
        "Failed" -> Color(0xFFFF5252)
        "Chargeback" -> Color(0xFFFF5252)
        "Refunded" -> Color(0xFF2196F3)
        else -> Color(0xFF888888)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, null, tint = Color(0xFFFF6B6B))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Back to Invoices", color = Color(0xFFFF6B6B))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Invoice Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("INVOICE", color = Color(0xFF888888), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("#${invoice.id}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            invoice.paymentStatus,
                            color = statusColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                if (invoice.isFlagged) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFF5252).copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Flag, null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("FLAGGED", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                if (invoice.flagReason.isNotBlank()) {
                                    Text(invoice.flagReason, color = Color(0xFFFF8A8A), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("CUSTOMER", color = Color(0xFF888888), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(invoice.userName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Member ID: ${invoice.userId}", color = Color(0xFF666666), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Item Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ITEM DETAILS", color = Color(0xFF888888), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Item", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(invoice.itemName, color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Quantity", color = Color(0xFF666666), fontSize = 12.sp)
                    Text("${invoice.quantity}", color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Unit Price", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(invoice.price, color = Color.White, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFF444444))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(invoice.price, color = Color(0xFF90EE90), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transaction Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TRANSACTION INFO", color = Color(0xFF888888), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Transaction ID", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(invoice.transactionId, color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment Method", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(invoice.paymentMethod, color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(formatDate(invoice.purchaseDate), color = Color.White, fontSize = 14.sp)
                }

                if (invoice.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Notes", color = Color(0xFF666666), fontSize = 12.sp)
                    Text(invoice.notes, color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Text("ACTIONS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { showStatusDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("STATUS", fontSize = 12.sp)
            }

            Button(
                onClick = { showSendReminderDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.NotificationsActive, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("REMIND", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (invoice.isFlagged) {
                Button(
                    onClick = { userViewModel.unflagInvoice(invoice.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("UNFLAG", fontSize = 12.sp)
                }
            } else {
                Button(
                    onClick = { showFlagDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Flag, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("FLAG", fontSize = 12.sp)
                }
            }

            OutlinedButton(
                onClick = { /* Print/Export */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF888888))
            ) {
                Icon(Icons.Default.Print, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PRINT", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Flag Dialog
    if (showFlagDialog) {
        var flagReason by remember { mutableStateOf("") }
        val reasons = listOf("Chargeback received", "Payment declined", "Suspicious activity", "Refund requested", "Other")

        AlertDialog(
            onDismissRequest = { showFlagDialog = false },
            title = { Text("Flag Invoice", color = Color.White) },
            text = {
                Column {
                    Text("Select reason for flagging:", color = Color(0xFFCCCCCC), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { flagReason = reason }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = flagReason == reason,
                                onClick = { flagReason = reason },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFF5252),
                                    unselectedColor = Color(0xFF888888)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(reason, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (flagReason.isNotBlank()) {
                            userViewModel.flagInvoice(invoice.id, flagReason)
                            showFlagDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    enabled = flagReason.isNotBlank()
                ) {
                    Text("FLAG INVOICE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFlagDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }

    // Send Reminder Dialog
    if (showSendReminderDialog) {
        AlertDialog(
            onDismissRequest = { showSendReminderDialog = false },
            title = { Text("Send Payment Reminder", color = Color.White) },
            text = {
                Column {
                    Text("Send a payment reminder to ${invoice.userName}?", color = Color(0xFFCCCCCC), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF333333),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Invoice #${invoice.id}", color = Color.White, fontSize = 13.sp)
                            Text("Amount: ${invoice.price}", color = Color(0xFF90EE90), fontSize = 13.sp)
                            Text("Item: ${invoice.itemName}", color = Color(0xFF888888), fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        userViewModel.sendPaymentReminder(invoice.userId, invoice.userName, invoice.id)
                        showSendReminderDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("SEND REMINDER")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSendReminderDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }

    // Update Status Dialog
    if (showStatusDialog) {
        var selectedStatus by remember { mutableStateOf(invoice.paymentStatus) }
        val statuses = listOf("Paid", "Pending", "Failed", "Chargeback", "Refunded")

        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Update Payment Status", color = Color.White) },
            text = {
                Column {
                    statuses.forEach { status ->
                        val statusColor = when (status) {
                            "Paid" -> Color(0xFF4CAF50)
                            "Pending" -> Color(0xFFFF9800)
                            "Failed" -> Color(0xFFFF5252)
                            "Chargeback" -> Color(0xFFFF5252)
                            "Refunded" -> Color(0xFF2196F3)
                            else -> Color(0xFF888888)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStatus = status }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatus == status,
                                onClick = { selectedStatus = status },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = statusColor,
                                    unselectedColor = Color(0xFF888888)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(status, color = statusColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        userViewModel.updateInvoiceStatus(invoice.id, selectedStatus)
                        showStatusDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
                ) {
                    Text("UPDATE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }
}

// ==================== DOCUMENTS TAB ====================
@Composable
fun DocumentsTab(userViewModel: UserViewModel) {
    var pendingDocs by remember { mutableStateOf(listOf(
        PendingDocument(1, "John Doe", "PAL License", "2 hours ago"),
        PendingDocument(2, "Jane Smith", "RPAL Certificate", "5 hours ago"),
        PendingDocument(3, "Mike Johnson", "Handgun Safety Qual", "1 day ago")
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("PENDING APPROVAL (${pendingDocs.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        if (pendingDocs.isEmpty()) {
            EmptyStateCard("✅", "All Caught Up", "No documents pending approval")
        } else {
            pendingDocs.forEach { doc ->
                PendingDocumentCard(
                    doc = doc,
                    onApprove = { pendingDocs = pendingDocs.filter { it.id != doc.id } },
                    onReject = { pendingDocs = pendingDocs.filter { it.id != doc.id } }
                )
            }
        }
    }
}

data class PendingDocument(val id: Int, val memberName: String, val docType: String, val uploadTime: String)

@Composable
fun PendingDocumentCard(doc: PendingDocument, onApprove: () -> Unit, onReject: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(doc.docType, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text("Uploaded by ${doc.memberName}", color = Color(0xFF888888), fontSize = 12.sp)
                    Text(doc.uploadTime, color = Color(0xFF666666), fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { /* View document */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("VIEW", fontSize = 12.sp)
                }
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("APPROVE", fontSize = 12.sp)
                }
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REJECT", fontSize = 12.sp)
                }
            }
        }
    }
}

// ==================== FORUM MODERATION TAB ====================
@Composable
fun ForumModerationTab(userViewModel: UserViewModel) {
    var flaggedPosts by remember { mutableStateOf(listOf(
        FlaggedPost(1, "Anonymous", "This is inappropriate content...", "3 reports"),
        FlaggedPost(2, "User123", "Spam message here...", "2 reports")
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("FLAGGED POSTS (${flaggedPosts.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        if (flaggedPosts.isEmpty()) {
            EmptyStateCard("✅", "No Flagged Posts", "All forum content is clean")
        } else {
            flaggedPosts.forEach { post ->
                FlaggedPostCard(
                    post = post,
                    onHide = { flaggedPosts = flaggedPosts.filter { it.id != post.id } },
                    onDismiss = { flaggedPosts = flaggedPosts.filter { it.id != post.id } }
                )
            }
        }
    }
}

data class FlaggedPost(val id: Int, val author: String, val content: String, val reports: String)

@Composable
fun FlaggedPostCard(post: FlaggedPost, onHide: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(post.author, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                Text(post.reports, color = Color(0xFFFF5252), fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(post.content, color = Color(0xFFCCCCCC), fontSize = 13.sp, maxLines = 3)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onHide,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.VisibilityOff, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("HIDE POST", fontSize = 12.sp)
                }
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DISMISS", fontSize = 12.sp)
                }
            }
        }
    }
}

// ==================== COURSES TAB ====================
@Composable
fun CoursesTab(userViewModel: UserViewModel) {
    var courses by remember { mutableStateOf(listOf(
        CourseItem(1, "PAL Course", "Canadian Firearms Safety Course", true, "Jan 25, 2026"),
        CourseItem(2, "RPAL Course", "Restricted Firearms Safety Course", true, "Feb 1, 2026"),
        CourseItem(3, "Handgun Safety", "Basic handgun safety qualification", true, "Jan 30, 2026"),
        CourseItem(4, "Pistol Qualification", "Advanced pistol certification", false, "TBD"),
        CourseItem(5, "New Member Orientation", "Club orientation for new members", true, "Every Saturday")
    )) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<CourseItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("MANAGE COURSES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("ADD COURSE", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        courses.forEach { course ->
            CourseManageCard(
                course = course,
                onToggle = {
                    courses = courses.map {
                        if (it.id == course.id) it.copy(isActive = !it.isActive) else it
                    }
                },
                onEdit = { showEditDialog = course },
                onDelete = { courses = courses.filter { it.id != course.id } }
            )
        }
    }

    if (showAddDialog) {
        AddCourseDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, desc, date ->
                courses = courses + CourseItem(courses.size + 1, name, desc, true, date)
                showAddDialog = false
            }
        )
    }

    showEditDialog?.let { course ->
        EditCourseDialog(
            course = course,
            onDismiss = { showEditDialog = null },
            onSave = { name, desc, date ->
                courses = courses.map {
                    if (it.id == course.id) it.copy(name = name, description = desc, nextDate = date) else it
                }
                showEditDialog = null
            }
        )
    }
}

data class CourseItem(val id: Int, val name: String, val description: String, val isActive: Boolean, val nextDate: String)

@Composable
fun CourseManageCard(course: CourseItem, onToggle: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(course.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(course.description, color = Color(0xFF888888), fontSize = 12.sp)
                    Text("Next: ${course.nextDate}", color = Color(0xFF90EE90), fontSize = 11.sp)
                }
                Switch(
                    checked = course.isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFF666666)
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2196F3))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("EDIT", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DELETE", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AddCourseDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Course", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Next Date") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onAdd(name, description, date) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
            ) {
                Text("ADD")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}

@Composable
fun EditCourseDialog(course: CourseItem, onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(course.name) }
    var description by remember { mutableStateOf(course.description) }
    var date by remember { mutableStateOf(course.nextDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Course", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Next Date") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, description, date) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}

// ==================== MESSAGES TAB ====================
@Composable
fun MessagesTab(userViewModel: UserViewModel) {
    var selectedMember by remember { mutableStateOf<User?>(null) }
    var messageText by remember { mutableStateOf("") }
    var sentMessages by remember { mutableStateOf(listOf<SentMessage>()) }
    val searchResults by userViewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getAllMembers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("SEND MESSAGE TO MEMBER", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        var expanded by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text(
                text = selectedMember?.fullName ?: "Select Member",
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF252525))
        ) {
            searchResults.forEach { member ->
                DropdownMenuItem(
                    text = { Text(member.fullName, color = Color.White) },
                    onClick = {
                        selectedMember = member
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Message") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB22222),
                unfocusedBorderColor = Color(0xFF444444),
                focusedLabelColor = Color(0xFFFF6B6B),
                unfocusedLabelColor = Color(0xFF888888),
                cursorColor = Color(0xFFFF6B6B),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (selectedMember != null && messageText.isNotBlank()) {
                    sentMessages = sentMessages + SentMessage(
                        selectedMember!!.fullName,
                        messageText,
                        "Just now"
                    )
                    messageText = ""
                    selectedMember = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
            shape = RoundedCornerShape(10.dp),
            enabled = selectedMember != null && messageText.isNotBlank()
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SEND MESSAGE", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("SENT MESSAGES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        if (sentMessages.isEmpty()) {
            EmptyStateCard("📧", "No Messages Sent", "Messages you send will appear here")
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                sentMessages.reversed().forEach { msg ->
                    SentMessageCard(msg)
                }
            }
        }
    }
}

data class SentMessage(val recipient: String, val message: String, val time: String)

@Composable
fun SentMessageCard(msg: SentMessage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF90EE90), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("To: ${msg.recipient}", color = Color(0xFF90EE90), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                Text(msg.time, color = Color(0xFF666666), fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(msg.message, color = Color(0xFFCCCCCC), fontSize = 13.sp)
        }
    }
}

// ==================== MEMBER DETAIL VIEW ====================
@Composable
fun MemberDetailView(
    member: User,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val purchases by userViewModel.getPurchasesForUser(member.id).collectAsState(initial = emptyList())
    val invoices by userViewModel.getInvoicesForUser(member.id).collectAsState(initial = emptyList())
    var showRefundDialog by remember { mutableStateOf<Purchase?>(null) }
    var showMessageDialog by remember { mutableStateOf(false) }
    var showUpdateCertsDialog by remember { mutableStateOf(false) }
    var showInvoiceDetail by remember { mutableStateOf<Invoice?>(null) }

    if (showInvoiceDetail != null) {
        InvoiceDetailView(
            invoice = showInvoiceDetail!!,
            userViewModel = userViewModel,
            onBack = { showInvoiceDetail = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFFFF6B6B))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Back to Members", color = Color(0xFFFF6B6B))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Member Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF007236), RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.fullName.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(member.fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(member.email, color = Color(0xFF888888), fontSize = 13.sp)
                        Text("Member #${member.memberNumber}", color = Color(0xFF90EE90), fontSize = 12.sp)
                        if (member.phone.isNotBlank()) {
                            Text(member.phone, color = Color(0xFF888888), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { showMessageDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("MESSAGE", fontSize = 11.sp)
            }
            Button(
                onClick = { showUpdateCertsDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CERTS", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Invoices Section
        Text("INVOICES & PAYMENT HISTORY", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (invoices.isEmpty()) {
            EmptyStateCard("🧾", "No Invoices", "This member has no payment history")
        } else {
            invoices.take(5).forEach { invoice ->
                InvoiceCard(
                    invoice = invoice,
                    onClick = { showInvoiceDetail = invoice }
                )
            }
            if (invoices.size > 5) {
                TextButton(
                    onClick = { /* Show all invoices */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View all ${invoices.size} invoices", color = Color(0xFF2196F3))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("ACTIVE PASSES & PURCHASES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (purchases.isEmpty()) {
            EmptyStateCard("🎫", "No Active Passes", "This member has no active passes")
        } else {
            purchases.forEach { purchase ->
                AdminPurchaseCard(
                    purchase = purchase,
                    onDeduct = { userViewModel.deductPurchase(purchase.id) },
                    onRefund = { showRefundDialog = purchase }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Refund Dialog
    showRefundDialog?.let { purchase ->
        AlertDialog(
            onDismissRequest = { showRefundDialog = null },
            title = { Text("Issue Refund", color = Color.White) },
            text = {
                Text(
                    "Issue refund for '${purchase.itemName}'?\n\nAmount: ${purchase.price}\nRemaining uses: ${purchase.remainingQuantity}",
                    color = Color(0xFFCCCCCC)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRefundDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222))
                ) {
                    Text("ISSUE REFUND")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRefundDialog = null }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }

    // Message Dialog
    if (showMessageDialog) {
        var messageText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showMessageDialog = false },
            title = { Text("Send Message to ${member.fullName}", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { showMessageDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
                ) {
                    Text("SEND")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMessageDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }

    // Update Certifications Dialog
    if (showUpdateCertsDialog) {
        UpdateCertificationsDialog(
            member = member,
            onDismiss = { showUpdateCertsDialog = false }
        )
    }
}

@Composable
fun UpdateCertificationsDialog(member: User, onDismiss: () -> Unit) {
    var hasPAL by remember { mutableStateOf(member.hasPAL) }
    var hasRPAL by remember { mutableStateOf(member.hasRPAL) }
    var hasCFSC by remember { mutableStateOf(member.hasCFSC) }
    var hasCRFSC by remember { mutableStateOf(member.hasCRFSC) }
    var hasOrientation by remember { mutableStateOf(member.hasNewMemberOrientation) }
    var hasHandgunSafety by remember { mutableStateOf(member.hasHandgunSafetyQual) }
    var hasPistolQual by remember { mutableStateOf(member.hasPistolQual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Certifications", color = Color.White) },
        text = {
            Column {
                CertToggleRow("PAL License", hasPAL) { hasPAL = it }
                CertToggleRow("RPAL License", hasRPAL) { hasRPAL = it }
                CertToggleRow("CFSC Certificate", hasCFSC) { hasCFSC = it }
                CertToggleRow("CRFSC Certificate", hasCRFSC) { hasCRFSC = it }
                CertToggleRow("New Member Orientation", hasOrientation) { hasOrientation = it }
                CertToggleRow("Handgun Safety Qual", hasHandgunSafety) { hasHandgunSafety = it }
                CertToggleRow("Pistol Qualification", hasPistolQual) { hasPistolQual = it }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}

@Composable
fun CertToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF666666)
            )
        )
    }
}

@Composable
fun AdminPurchaseCard(purchase: Purchase, onDeduct: () -> Unit, onRefund: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = purchase.itemName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(
                            if (purchase.remainingQuantity > 3) Color(0xFF007236) else Color(0xFFFF9800),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${purchase.remainingQuantity} left",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { purchase.remainingQuantity.toFloat() / purchase.totalQuantity.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF90EE90),
                trackColor = Color(0xFF444444)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Price: ${purchase.price} | Used: ${purchase.totalQuantity - purchase.remainingQuantity} of ${purchase.totalQuantity}",
                color = Color(0xFF888888),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showConfirm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    enabled = purchase.remainingQuantity > 0
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DEDUCT", fontSize = 12.sp)
                }
                Button(
                    onClick = onRefund,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CurrencyExchange, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REFUND", fontSize = 12.sp)
                }
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Confirm Deduction", color = Color.White) },
            text = {
                Text(
                    "Deduct 1 use from '${purchase.itemName}'?\n\nRemaining after: ${purchase.remainingQuantity - 1}",
                    color = Color(0xFFCCCCCC)
                )
            },
            confirmButton = {
                TextButton(onClick = { onDeduct(); showConfirm = false }) {
                    Text("CONFIRM", color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }
}

// ==================== HELPER COMPOSABLES ====================
@Composable
fun EmptyStateCard(emoji: String, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.White, fontSize = 16.sp)
            Text(subtitle, color = Color(0xFF888888), fontSize = 13.sp)
        }
    }
}

@Composable
fun MemberCard(member: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF007236), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.fullName.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.fullName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Member #${member.memberNumber}", color = Color(0xFF888888), fontSize = 12.sp)
                Text(member.email, color = Color(0xFF666666), fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF888888))
        }
    }
}