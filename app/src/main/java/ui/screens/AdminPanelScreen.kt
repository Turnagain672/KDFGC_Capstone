package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.AdminNotification
import com.example.capstone2.data.Invoice
import com.example.capstone2.data.User
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Notifications", "Members", "Invoices", "Courses")
    val notifications by userViewModel.activeNotifications.collectAsState(initial = emptyList())
    val unreadCount by userViewModel.unreadCount.collectAsState(initial = 0)
    var selectedMemberId by remember { mutableStateOf<Int?>(null) }
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }

    if (selectedMemberId != null) {
        MemberDetailView(userId = selectedMemberId!!, userViewModel = userViewModel, onBack = { selectedMemberId = null })
        return
    }
    if (selectedInvoice != null) {
        InvoiceDetailView(invoice = selectedInvoice!!, userViewModel = userViewModel, onBack = { selectedInvoice = null })
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Admin Panel", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (unreadCount > 0) {
                Badge(containerColor = Color(0xFFFF6B6B)) { Text("$unreadCount") }
            }
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, containerColor = Color(0xFF252525), contentColor = Color.White, edgePadding = 8.dp) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index },
                    text = { Text(title, color = if (selectedTab == index) Color(0xFF90EE90) else Color(0xFF888888)) })
            }
        }
        when (selectedTab) {
            0 -> NotificationsContent(userViewModel, notifications, { selectedMemberId = it }, { selectedTab = 2 })
            1 -> MembersContent(userViewModel) { selectedMemberId = it }
            2 -> InvoicesContent(userViewModel) { selectedInvoice = it }
            3 -> CoursesContent()
        }
    }
}

@Composable
private fun NotificationsContent(vm: UserViewModel, notifications: List<AdminNotification>, onMember: (Int) -> Unit, onInvoice: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("RECENT NOTIFICATIONS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { vm.markAllNotificationsAsRead() }) { Text("Mark All Read", color = Color(0xFF90EE90), fontSize = 12.sp) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (notifications.isEmpty()) {
            EmptyBox("📭", "No Notifications", "You're all caught up!")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notifications) { n -> NotificationItem(n, vm, onMember, onInvoice) }
            }
        }
    }
}

@Composable
private fun NotificationItem(n: AdminNotification, vm: UserViewModel, onMember: (Int) -> Unit, onInvoice: () -> Unit) {
    val icon = when (n.type) { "document" -> Icons.Default.Description; "purchase" -> Icons.Default.ShoppingCart; "member" -> Icons.Default.PersonAdd; "alert" -> Icons.Default.Warning; "chargeback" -> Icons.Default.CreditCard; else -> Icons.Default.Notifications }
    val iconColor = when (n.type) { "document" -> Color(0xFF2196F3); "purchase" -> Color(0xFF4CAF50); "member" -> Color(0xFF9C27B0); "alert" -> Color(0xFFFF9800); "chargeback" -> Color(0xFFF44336); else -> Color(0xFF888888) }
    Card(modifier = Modifier.fillMaxWidth().clickable {
        vm.markNotificationAsRead(n.id)
        when { n.relatedUserId != null && (n.type == "member" || n.type == "document") -> onMember(n.relatedUserId); n.type == "purchase" || n.type == "chargeback" -> onInvoice() }
    }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (n.isRead) Color(0xFF252525) else Color(0xFF2A2A2A))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(n.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(n.message, color = Color(0xFF888888), fontSize = 12.sp)
                Text(timeAgo(n.timestamp), color = Color(0xFF666666), fontSize = 10.sp)
            }
            if (!n.isRead) { Box(modifier = Modifier.size(8.dp).background(Color(0xFF90EE90), RoundedCornerShape(4.dp))) }
            IconButton(onClick = { vm.archiveNotification(n.id) }) { Icon(Icons.Default.Archive, contentDescription = "Archive", tint = Color(0xFF666666), modifier = Modifier.size(18.dp)) }
        }
    }
}

@Composable
private fun MembersContent(vm: UserViewModel, onMember: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    val members by vm.searchResults.collectAsState()
    LaunchedEffect(Unit) { vm.getAllMembers() }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = query, onValueChange = { query = it; if (it.isNotEmpty()) vm.searchMembers(it) else vm.getAllMembers() },
            label = { Text("Search members") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White),
            shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        Text("${members.size} MEMBERS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (members.isEmpty()) { EmptyBox("👥", "No Members Found", "Try a different search") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(members) { m -> MemberItem(m) { onMember(m.id) } } } }
    }
}

@Composable
private fun MemberItem(m: User, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(Color(0xFF007236), RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) {
                Text(m.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(m.fullName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Member #${m.memberNumber}", color = Color(0xFF90EE90), fontSize = 12.sp)
                Text(m.email, color = Color(0xFF666666), fontSize = 11.sp)
            }
            if (m.isAdmin) { Badge(containerColor = Color(0xFFFF6B6B)) { Text("Admin", fontSize = 10.sp) } }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF666666))
        }
    }
}

@Composable
private fun InvoicesContent(vm: UserViewModel, onInvoice: (Invoice) -> Unit) {
    val invoices by vm.allInvoices.collectAsState(initial = emptyList())
    val flagged by vm.flaggedInvoices.collectAsState(initial = emptyList())
    var flaggedOnly by remember { mutableStateOf(false) }
    val list = if (flaggedOnly) flagged else invoices
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("INVOICES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Flagged Only", color = Color(0xFF888888), fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = flaggedOnly, onCheckedChange = { flaggedOnly = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFFF6B6B)))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (list.isEmpty()) { EmptyBox("🧾", "No Invoices", if (flaggedOnly) "No flagged invoices" else "No invoices yet") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(list) { i -> InvoiceItem(i) { onInvoice(i) } } } }
    }
}

@Composable
private fun InvoiceItem(i: Invoice, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (i.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text(i.itemName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold); Text(i.userName, color = Color(0xFF888888), fontSize = 12.sp) }
                Column(horizontalAlignment = Alignment.End) { Text(i.price, color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold); Text(dateFormat(i.purchaseDate), color = Color(0xFF666666), fontSize = 10.sp) }
            }
            if (i.isFlagged) { Spacer(modifier = Modifier.height(8.dp)); Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(14.dp)); Spacer(modifier = Modifier.width(4.dp)); Text(i.flagReason, color = Color(0xFFFF6B6B), fontSize = 11.sp) } }
        }
    }
}

@Composable
private fun CoursesContent() {
    val courses = listOf(
        Triple(1, "PAL Course" to "Canadian Firearms Safety Course", "Feb 15, 2026"),
        Triple(2, "RPAL Course" to "Restricted Firearms Safety", "Feb 22, 2026"),
        Triple(3, "Handgun Safety" to "Pistol qualification course", "Mar 1, 2026"),
        Triple(4, "New Member Orientation" to "Club rules and range safety", "Weekly")
    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("MANAGE COURSES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(courses) { c ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(c.second.first, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(c.second.second, color = Color(0xFF888888), fontSize = 12.sp)
                        Text("Next: ${c.third}", color = Color(0xFF90EE90), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberDetailView(userId: Int, userViewModel: UserViewModel, onBack: () -> Unit) {
    var member by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(userId) { member = userViewModel.getUserById(userId) }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("Member Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (member == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFF90EE90)) }
        } else {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(60.dp).background(Color(0xFF007236), RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) {
                            Text(member!!.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(member!!.fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Member #${member!!.memberNumber}", color = Color(0xFF90EE90), fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFF444444))
                    Spacer(modifier = Modifier.height(20.dp))
                    AdminInfoRow("Email", member!!.email)
                    AdminInfoRow("Phone", member!!.phone.ifEmpty { "Not provided" })
                    AdminInfoRow("Member Type", member!!.membershipType)
                    AdminInfoRow("PAL Number", member!!.palNumber.ifEmpty { "Not provided" })
                    AdminInfoRow("Admin", if (member!!.isAdmin) "Yes" else "No")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("MESSAGE")
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("EDIT")
                }
            }
        }
    }
}

@Composable
private fun InvoiceDetailView(invoice: Invoice, userViewModel: UserViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("Invoice Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (invoice.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Invoice #${invoice.id}", color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(invoice.paymentStatus, color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFF444444))
                Spacer(modifier = Modifier.height(20.dp))
                AdminInfoRow("Item", invoice.itemName)
                AdminInfoRow("Customer", invoice.userName)
                AdminInfoRow("Quantity", invoice.quantity.toString())
                AdminInfoRow("Price", invoice.price)
                AdminInfoRow("Transaction ID", invoice.transactionId)
                AdminInfoRow("Date", dateFormat(invoice.purchaseDate))
                AdminInfoRow("Payment Method", invoice.paymentMethod)
                if (invoice.isFlagged) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Flagged: ${invoice.flagReason}", color = Color(0xFFFF6B6B), fontSize = 13.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (invoice.isFlagged) {
                Button(onClick = { userViewModel.unflagInvoice(invoice.id); onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) { Text("UNFLAG") }
            } else {
                Button(onClick = { userViewModel.flagInvoice(invoice.id, "Review needed"); onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)), modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) { Text("FLAG") }
            }
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) { Text("REFUND") }
        }
    }
}

@Composable
private fun AdminInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF888888), fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
private fun EmptyBox(emoji: String, title: String, subtitle: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.White, fontSize = 16.sp)
            Text(subtitle, color = Color(0xFF888888), fontSize = 13.sp)
        }
    }
}

private fun timeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun dateFormat(timestamp: Long): String {
    return SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
}