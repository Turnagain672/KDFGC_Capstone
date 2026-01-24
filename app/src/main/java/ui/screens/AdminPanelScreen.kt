package com.example.capstone2.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.*

@Composable
private fun EmptyBox(emoji: String, title: String, subtitle: String) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.White, fontSize = 16.sp)
            Text(subtitle, color = Color(0xFF888888), fontSize = 13.sp)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Notifications", "Members", "Invoices", "Courses", "News", "Users", "Forum")
    val notifications by userViewModel.activeNotifications.collectAsState(initial = emptyList())
    val unreadCount by userViewModel.unreadCount.collectAsState(initial = 0)
    var selectedMemberId by remember { mutableStateOf<Int?>(null) }
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }
    var selectedNews by remember { mutableStateOf<News?>(null) }
    var showExpiryMemberList by remember { mutableStateOf(false) }
    var selectedExpiryMember by remember { mutableStateOf<User?>(null) }
    var expiryMessageSubject by remember { mutableStateOf("Certification Expiry Reminder") }
    var expiryMessageBody by remember { mutableStateOf("Dear Member,\n\nYour certification is expiring soon. Please rebook your course or renew your license to maintain your range access.\n\nContact us if you have any questions.\n\nBest regards,\nKDFGC Admin Team") }
    val allMembers by userViewModel.searchResults.collectAsState()

    LaunchedEffect(showExpiryMemberList) { if (showExpiryMemberList) userViewModel.getAllMembers() }

    BackHandler(enabled = selectedMemberId != null || selectedInvoice != null || selectedExpiryMember != null || selectedNews != null) {
        when {
            selectedExpiryMember != null -> selectedExpiryMember = null
            selectedMemberId != null -> selectedMemberId = null
            selectedInvoice != null -> selectedInvoice = null
            selectedNews != null -> selectedNews = null
        }
    }

    if (selectedMemberId != null) { MemberDetailView(userId = selectedMemberId!!, userViewModel = userViewModel, onBack = { selectedMemberId = null }); return }
    if (selectedInvoice != null) { InvoiceDetailView(invoice = selectedInvoice!!, userViewModel = userViewModel, onBack = { selectedInvoice = null }, onRefresh = { selectedInvoice = null }); return }
    if (selectedNews != null) { NewsDetailView(news = selectedNews!!, userViewModel = userViewModel, onBack = { selectedNews = null }); return }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("Admin Panel", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (unreadCount > 0) { Badge(containerColor = Color(0xFFFF6B6B)) { Text("$unreadCount") } }
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, containerColor = Color(0xFF252525), contentColor = Color.White, edgePadding = 8.dp) {
            tabs.forEachIndexed { index, title -> Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title, color = if (selectedTab == index) Color(0xFF90EE90) else Color(0xFF888888)) }) }
        }
        when (selectedTab) {
            0 -> NotificationsContent(vm = userViewModel, notifications = notifications, onMember = { selectedMemberId = it }, onInvoice = { selectedTab = 2 }, onExpiryAlert = { showExpiryMemberList = true })
            1 -> MembersContent(userViewModel) { selectedMemberId = it }
            2 -> InvoicesContent(userViewModel) { selectedInvoice = it }
            3 -> CoursesContent(userViewModel)
            4 -> NewsContent(userViewModel) { selectedNews = it }
            5 -> UsersContent(userViewModel)
            6 -> ForumModerationContent(userViewModel)
        }
    }

    if (showExpiryMemberList && selectedExpiryMember == null) {
        AlertDialog(onDismissRequest = { showExpiryMemberList = false }, containerColor = Color(0xFF252525),
            title = { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Warning, null, tint = Color(0xFFFF9800), modifier = Modifier.size(24.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Select Member", color = Color.White, fontWeight = FontWeight.Bold) } },
            text = { Column(modifier = Modifier.height(300.dp)) { Text("Choose member to send expiry reminder:", color = Color(0xFF90EE90), fontSize = 13.sp); Spacer(modifier = Modifier.height(12.dp)); LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(allMembers.filter { !it.isAdmin }) { m -> Card(modifier = Modifier.fillMaxWidth().clickable { selectedExpiryMember = m }, colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))) { Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(36.dp).background(Color(0xFFFF9800), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) { Text(m.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp) }; Spacer(modifier = Modifier.width(12.dp)); Column { Text(m.fullName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp); Text(m.email, color = Color(0xFF888888), fontSize = 11.sp) } } } } } } },
            confirmButton = { }, dismissButton = { OutlinedButton(onClick = { showExpiryMemberList = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (selectedExpiryMember != null) {
        AlertDialog(onDismissRequest = { selectedExpiryMember = null }, containerColor = Color(0xFF252525),
            title = { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Warning, null, tint = Color(0xFFFF9800), modifier = Modifier.size(24.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Send to ${selectedExpiryMember!!.fullName}", color = Color.White, fontWeight = FontWeight.Bold) } },
            text = { Column { Text("Send expiry reminder to this member", color = Color(0xFF90EE90), fontSize = 13.sp); Spacer(modifier = Modifier.height(16.dp)); OutlinedTextField(value = expiryMessageSubject, onValueChange = { expiryMessageSubject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF9800), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true); Spacer(modifier = Modifier.height(12.dp)); OutlinedTextField(value = expiryMessageBody, onValueChange = { expiryMessageBody = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(180.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF9800), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White)) } },
            confirmButton = { Button(onClick = { userViewModel.sendAdminMessage(expiryMessageSubject, expiryMessageBody, listOf(selectedExpiryMember!!.id)); selectedExpiryMember = null; showExpiryMemberList = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) { Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("SEND") } },
            dismissButton = { OutlinedButton(onClick = { selectedExpiryMember = null }) { Text("BACK", color = Color(0xFF888888)) } })
    }
}
@Composable
private fun NotificationsContent(vm: UserViewModel, notifications: List<AdminNotification>, onMember: (Int) -> Unit, onInvoice: () -> Unit, onExpiryAlert: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("RECENT NOTIFICATIONS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold); TextButton(onClick = { vm.markAllNotificationsAsRead() }) { Text("Mark All Read", color = Color(0xFF90EE90), fontSize = 12.sp) } }
        Spacer(modifier = Modifier.height(12.dp))
        if (notifications.isEmpty()) { EmptyBox("📭", "No Notifications", "You're all caught up!") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(notifications) { n -> NotificationItem(n, vm, onMember, onInvoice, onExpiryAlert) } } }
    }
}

@Composable
private fun NotificationItem(n: AdminNotification, vm: UserViewModel, onMember: (Int) -> Unit, onInvoice: () -> Unit, onExpiryAlert: () -> Unit) {
    val icon = when (n.type) { "document" -> Icons.Default.Description; "purchase" -> Icons.Default.ShoppingCart; "member" -> Icons.Default.PersonAdd; "alert" -> Icons.Default.Warning; "invoice" -> Icons.Default.Receipt; "refund" -> Icons.Default.Refresh; else -> Icons.Default.Notifications }
    val iconColor = when (n.type) { "document" -> Color(0xFF2196F3); "purchase" -> Color(0xFF4CAF50); "member" -> Color(0xFF9C27B0); "alert" -> Color(0xFFFF9800); "invoice" -> Color(0xFF00BCD4); "refund" -> Color(0xFFE91E63); else -> Color(0xFF888888) }
    Card(modifier = Modifier.fillMaxWidth().clickable { vm.markNotificationAsRead(n.id); when { n.type == "alert" && n.title.contains("Expiry") -> onExpiryAlert(); n.type == "invoice" || n.type == "refund" -> onInvoice(); n.relatedUserId != null -> onMember(n.relatedUserId); else -> onInvoice() } }, colors = CardDefaults.cardColors(containerColor = if (n.isRead) Color(0xFF252525) else Color(0xFF2A2A2A))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text(n.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold); Text(n.message, color = Color(0xFF888888), fontSize = 12.sp) }
            if (!n.isRead) { Box(modifier = Modifier.size(8.dp).background(Color(0xFF90EE90), RoundedCornerShape(4.dp))) }
        }
    }
}

@Composable
private fun MembersContent(vm: UserViewModel, onMember: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    val members by vm.searchResults.collectAsState()
    LaunchedEffect(Unit) { vm.getAllMembers() }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = query, onValueChange = { query = it; if (it.isNotEmpty()) vm.searchMembers(it) else vm.getAllMembers() }, label = { Text("Search members") }, leadingIcon = { Icon(Icons.Default.Search, null) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        Text("${members.size} MEMBERS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (members.isEmpty()) { EmptyBox("👥", "No Members Found", "Try a different search") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(members) { m -> MemberItem(m) { onMember(m.id) } } } }
    }
}

@Composable
private fun MemberItem(m: User, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(Color(0xFF007236), RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) { Text(m.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(m.fullName, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text("Member #${m.memberNumber}", color = Color(0xFF90EE90), fontSize = 12.sp)
                Text(m.email, color = Color(0xFF666666), fontSize = 11.sp)
            }
            if (m.isAdmin) { Badge(containerColor = Color(0xFFFF6B6B)) { Text("Admin", fontSize = 10.sp) } }
            Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF666666))
        }
    }
}
@Composable
private fun InvoicesContent(vm: UserViewModel, onInvoice: (Invoice) -> Unit) {
    val context = LocalContext.current
    val invoices by vm.allInvoices.collectAsState(initial = emptyList())
    var flaggedOnly by remember { mutableStateOf(false) }
    val flagged by vm.flaggedInvoices.collectAsState(initial = emptyList())
    val list = if (flaggedOnly) flagged else invoices
    var showCreateDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    val allMembers by vm.searchResults.collectAsState()

    LaunchedEffect(Unit) { vm.getAllMembers() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("INVOICES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { showExportDialog = true }) { Icon(Icons.Default.Download, null, tint = Color(0xFF90EE90)) }
                Button(onClick = { showCreateDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) { Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("NEW", fontSize = 12.sp) }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) { Text("Flagged Only", color = Color(0xFF888888), fontSize = 12.sp); Spacer(modifier = Modifier.width(8.dp)); Switch(checked = flaggedOnly, onCheckedChange = { flaggedOnly = it }) }
        Spacer(modifier = Modifier.height(12.dp))
        if (list.isEmpty()) { EmptyBox("🧾", "No Invoices", "No invoices yet") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(list) { i -> InvoiceItem(i) { onInvoice(i) } } } }
    }

    if (showCreateDialog) { CreateInvoiceDialog(vm = vm, members = allMembers.filter { !it.isAdmin }, onDismiss = { showCreateDialog = false }) }

    if (showExportDialog) {
        val exportText = vm.exportAllInvoices(invoices)
        AlertDialog(onDismissRequest = { showExportDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Export Invoices", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Column { Text("${invoices.size} invoices ready to export", color = Color(0xFF90EE90), fontSize = 14.sp); Spacer(modifier = Modifier.height(12.dp)); Card(modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))) { Column(modifier = Modifier.padding(12.dp).verticalScroll(rememberScrollState())) { Text(exportText, color = Color(0xFF888888), fontSize = 11.sp) } } } },
            confirmButton = { Button(onClick = { val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager; clipboard.setPrimaryClip(ClipData.newPlainText("Invoices", exportText)); Toast.makeText(context, "Invoices copied to clipboard!", Toast.LENGTH_SHORT).show(); showExportDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Icon(Icons.Default.ContentCopy, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("COPY") } },
            dismissButton = { OutlinedButton(onClick = { showExportDialog = false }) { Text("CLOSE", color = Color(0xFF888888)) } })
    }
}

@Composable
private fun CreateInvoiceDialog(vm: UserViewModel, members: List<User>, onDismiss: () -> Unit) {
    var selectedMember by remember { mutableStateOf<User?>(null) }
    var itemName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var message by remember { mutableStateOf("") }
    var showMemberPicker by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = onDismiss, containerColor = Color(0xFF252525),
        title = { Text("Create Custom Invoice", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Send invoice to member", color = Color(0xFF90EE90), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth().clickable { showMemberPicker = true }, colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))) { Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Person, null, tint = Color(0xFF888888)); Spacer(modifier = Modifier.width(12.dp)); Text(selectedMember?.fullName ?: "Select Member", color = if (selectedMember != null) Color.White else Color(0xFF888888), modifier = Modifier.weight(1f)); Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFF888888)) } }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = itemName, onValueChange = { itemName = it }, label = { Text("Item/Service Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                    OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Qty") }, modifier = Modifier.weight(0.5f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message (optional)") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
            }
        },
        confirmButton = { Button(onClick = { selectedMember?.let { member -> vm.createCustomInvoice(member.id, member.fullName, itemName, if (price.startsWith("$")) price else "$$price", quantity.toIntOrNull() ?: 1, message); onDismiss() } }, enabled = selectedMember != null && itemName.isNotBlank() && price.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("SEND INVOICE") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("CANCEL", color = Color(0xFF888888)) } })

    if (showMemberPicker) {
        AlertDialog(onDismissRequest = { showMemberPicker = false }, containerColor = Color(0xFF252525),
            title = { Text("Select Member", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { LazyColumn(modifier = Modifier.height(300.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(members) { m -> Card(modifier = Modifier.fillMaxWidth().clickable { selectedMember = m; showMemberPicker = false }, colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))) { Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(36.dp).background(Color(0xFF007236), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) { Text(m.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp) }; Spacer(modifier = Modifier.width(12.dp)); Column { Text(m.fullName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp); Text(m.email, color = Color(0xFF888888), fontSize = 11.sp) } } } } } },
            confirmButton = { },
            dismissButton = { OutlinedButton(onClick = { showMemberPicker = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}

@Composable
private fun InvoiceItem(i: Invoice, onClick: () -> Unit) {
    val statusColor = when (i.paymentStatus) { "Paid" -> Color(0xFF4CAF50); "Pending" -> Color(0xFFFF9800); "Refunded" -> Color(0xFF9C27B0); else -> Color(0xFF888888) }
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = if (i.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) { Text(i.itemName, color = Color.White, fontWeight = FontWeight.SemiBold); Text(i.userName, color = Color(0xFF888888), fontSize = 12.sp) }
                Column(horizontalAlignment = Alignment.End) { Text(i.price, color = Color(0xFF90EE90), fontWeight = FontWeight.Bold); Text(i.paymentStatus, color = statusColor, fontSize = 11.sp) }
            }
            if (i.isFlagged) { Spacer(modifier = Modifier.height(8.dp)); Text("⚠ ${i.flagReason}", color = Color(0xFFFF6B6B), fontSize = 11.sp) }
        }
    }
}
@Composable
private fun CoursesContent(vm: UserViewModel) {
    val courses by vm.allCourses.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var courseName by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }
    var courseDate by remember { mutableStateOf("") }
    var courseTime by remember { mutableStateOf("") }
    var courseCost by remember { mutableStateOf("") }
    var courseClassSize by remember { mutableStateOf("20") }
    var courseInstructor by remember { mutableStateOf("") }
    var courseEmail by remember { mutableStateOf("") }
    var courseLocation by remember { mutableStateOf("KDFGC Clubhouse") }

    fun resetFields() { courseName = ""; courseDescription = ""; courseDate = ""; courseTime = ""; courseCost = ""; courseClassSize = "20"; courseInstructor = ""; courseEmail = ""; courseLocation = "KDFGC Clubhouse" }
    fun loadCourse(c: Course) { courseName = c.name; courseDescription = c.description; courseDate = c.date; courseTime = c.time; courseCost = c.cost; courseClassSize = c.classSize.toString(); courseInstructor = c.instructorName; courseEmail = c.instructorEmail; courseLocation = c.location }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("MANAGE COURSES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { resetFields(); showAddDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) { Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("ADD", fontSize = 12.sp) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (courses.isEmpty()) { EmptyBox("📚", "No Courses", "Add your first course") }
        else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(courses) { c ->
                    Card(modifier = Modifier.fillMaxWidth().clickable { selectedCourse = c; loadCourse(c); showEditDialog = true }, colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column(modifier = Modifier.weight(1f)) { Text(c.name, color = Color.White, fontWeight = FontWeight.SemiBold); Text("${c.date} • ${c.time}", color = Color(0xFF90EE90), fontSize = 12.sp) }; Text(c.cost, color = Color(0xFF90EE90), fontWeight = FontWeight.Bold) }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Seats: ${c.seatsRemaining}/${c.classSize}", color = Color(0xFF888888), fontSize = 12.sp); Text(c.location, color = Color(0xFF888888), fontSize = 12.sp) }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { selectedCourse = c; loadCourse(c); showEditDialog = true }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp), tint = Color(0xFF90EE90)); Spacer(modifier = Modifier.width(4.dp)); Text("EDIT", fontSize = 11.sp, color = Color(0xFF90EE90)) }
                                OutlinedButton(onClick = { selectedCourse = c; showDeleteDialog = true }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(14.dp), tint = Color(0xFFFF6B6B)); Spacer(modifier = Modifier.width(4.dp)); Text("DELETE", fontSize = 11.sp, color = Color(0xFFFF6B6B)) }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(onDismissRequest = { showAddDialog = false }, containerColor = Color(0xFF252525), title = { Text("Add New Course", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = courseName, onValueChange = { courseName = it }, label = { Text("Course Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseDescription, onValueChange = { courseDescription = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = courseDate, onValueChange = { courseDate = it }, label = { Text("Date") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                        OutlinedTextField(value = courseTime, onValueChange = { courseTime = it }, label = { Text("Time") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = courseCost, onValueChange = { courseCost = it }, label = { Text("Cost") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                        OutlinedTextField(value = courseClassSize, onValueChange = { courseClassSize = it }, label = { Text("Class Size") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseInstructor, onValueChange = { courseInstructor = it }, label = { Text("Instructor Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseEmail, onValueChange = { courseEmail = it }, label = { Text("Instructor Email") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseLocation, onValueChange = { courseLocation = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                }
            },
            confirmButton = { Button(onClick = { vm.addCourse(courseName, courseDescription, courseDate, courseTime, courseCost, courseClassSize.toIntOrNull() ?: 20, courseInstructor, courseEmail, courseLocation, ""); showAddDialog = false; resetFields() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("ADD COURSE") } },
            dismissButton = { OutlinedButton(onClick = { showAddDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showEditDialog && selectedCourse != null) {
        AlertDialog(onDismissRequest = { showEditDialog = false }, containerColor = Color(0xFF252525), title = { Text("Edit Course", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = courseName, onValueChange = { courseName = it }, label = { Text("Course Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseDescription, onValueChange = { courseDescription = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = courseDate, onValueChange = { courseDate = it }, label = { Text("Date") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                        OutlinedTextField(value = courseTime, onValueChange = { courseTime = it }, label = { Text("Time") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = courseCost, onValueChange = { courseCost = it }, label = { Text("Cost") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                        OutlinedTextField(value = courseClassSize, onValueChange = { courseClassSize = it }, label = { Text("Class Size") }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseInstructor, onValueChange = { courseInstructor = it }, label = { Text("Instructor Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseEmail, onValueChange = { courseEmail = it }, label = { Text("Instructor Email") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = courseLocation, onValueChange = { courseLocation = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                }
            },
            confirmButton = { Button(onClick = { selectedCourse?.let { c -> vm.updateCourse(c.copy(name = courseName, description = courseDescription, date = courseDate, time = courseTime, cost = courseCost, classSize = courseClassSize.toIntOrNull() ?: c.classSize, instructorName = courseInstructor, instructorEmail = courseEmail, location = courseLocation)) }; showEditDialog = false; resetFields() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("SAVE CHANGES") } },
            dismissButton = { OutlinedButton(onClick = { showEditDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showDeleteDialog && selectedCourse != null) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false }, containerColor = Color(0xFF252525), title = { Text("Delete Course", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete \"${selectedCourse!!.name}\"? This action cannot be undone.", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { selectedCourse?.let { vm.deleteCourse(it) }; showDeleteDialog = false; selectedCourse = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) { Text("DELETE") } },
            dismissButton = { OutlinedButton(onClick = { showDeleteDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}
@Composable
private fun NewsContent(vm: UserViewModel, onNews: (News) -> Unit) {
    val news by vm.allNews.collectAsState(initial = emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }
    var newsTitle by remember { mutableStateOf("") }
    var newsSummary by remember { mutableStateOf("") }
    var newsContent by remember { mutableStateOf("") }
    var isFeatured by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("MANAGE NEWS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { newsTitle = ""; newsSummary = ""; newsContent = ""; isFeatured = true; showCreateDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) { Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("NEW", fontSize = 12.sp) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (news.isEmpty()) { EmptyBox("📰", "No News Articles", "Create your first news post") }
        else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(news) { n ->
                    Card(modifier = Modifier.fillMaxWidth().clickable { onNews(n) }, colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(n.title, color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (n.isFeatured) { Badge(containerColor = Color(0xFFFF9800)) { Text("Featured", fontSize = 9.sp) } }
                                    if (n.isPublished) { Badge(containerColor = Color(0xFF4CAF50)) { Text("Live", fontSize = 9.sp) } }
                                    else { Badge(containerColor = Color(0xFF888888)) { Text("Draft", fontSize = 9.sp) } }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(n.summary, color = Color(0xFF888888), fontSize = 12.sp, maxLines = 2)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("By ${n.authorName} • ${java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(java.util.Date(n.createdAt))}", color = Color(0xFF666666), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(onDismissRequest = { showCreateDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Create News Article", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = newsTitle, onValueChange = { newsTitle = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newsSummary, onValueChange = { newsSummary = it }, label = { Text("Summary (short preview)") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newsContent, onValueChange = { newsContent = it }, label = { Text("Full Content") }, modifier = Modifier.fillMaxWidth().height(150.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it }); Spacer(modifier = Modifier.width(8.dp)); Text("Feature on Home Screen", color = Color.White) }
                }
            },
            confirmButton = { Button(onClick = { vm.createNews(newsTitle, newsSummary, newsContent, isFeatured); showCreateDialog = false }, enabled = newsTitle.isNotBlank() && newsContent.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("PUBLISH") } },
            dismissButton = { OutlinedButton(onClick = { showCreateDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}

@Composable
private fun NewsDetailView(news: News, userViewModel: UserViewModel, onBack: () -> Unit) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf(news.title) }
    var editSummary by remember { mutableStateOf(news.summary) }
    var editContent by remember { mutableStateOf(news.content) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }; Text("News Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (news.isFeatured) { Badge(containerColor = Color(0xFFFF9800)) { Text("Featured", fontSize = 10.sp) } }
                        if (news.isPublished) { Badge(containerColor = Color(0xFF4CAF50)) { Text("Published", fontSize = 10.sp) } }
                        else { Badge(containerColor = Color(0xFF888888)) { Text("Draft", fontSize = 10.sp) } }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(news.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("By ${news.authorName} • ${java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(news.createdAt))}", color = Color(0xFF90EE90), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp)); HorizontalDivider(color = Color(0xFF444444)); Spacer(modifier = Modifier.height(16.dp))
                    Text(news.summary, color = Color(0xFFAAAAAA), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(news.content, color = Color.White, fontSize = 14.sp, lineHeight = 22.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { userViewModel.setNewsFeatured(news.id, !news.isFeatured) }, colors = ButtonDefaults.buttonColors(containerColor = if (news.isFeatured) Color(0xFF888888) else Color(0xFFFF9800)), modifier = Modifier.weight(1f)) { Text(if (news.isFeatured) "UNFEATURE" else "FEATURE") }
                Button(onClick = { userViewModel.setNewsPublished(news.id, !news.isPublished) }, colors = ButtonDefaults.buttonColors(containerColor = if (news.isPublished) Color(0xFF888888) else Color(0xFF4CAF50)), modifier = Modifier.weight(1f)) { Text(if (news.isPublished) "UNPUBLISH" else "PUBLISH") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { showEditDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("EDIT") }
                Button(onClick = { showDeleteDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("DELETE") }
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(onDismissRequest = { showEditDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Edit News", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = editTitle, onValueChange = { editTitle = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editSummary, onValueChange = { editSummary = it }, label = { Text("Summary") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editContent, onValueChange = { editContent = it }, label = { Text("Content") }, modifier = Modifier.fillMaxWidth().height(150.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                }
            },
            confirmButton = { Button(onClick = { userViewModel.updateNews(news.copy(title = editTitle, summary = editSummary, content = editContent)); showEditDialog = false; onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("SAVE") } },
            dismissButton = { OutlinedButton(onClick = { showEditDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showDeleteDialog) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Delete News", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete \"${news.title}\"?", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { userViewModel.deleteNews(news); showDeleteDialog = false; onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) { Text("DELETE") } },
            dismissButton = { OutlinedButton(onClick = { showDeleteDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}
@Composable
private fun UsersContent(vm: UserViewModel) {
    val users by vm.allUsers.collectAsState(initial = emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }
    var showRoleDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var newEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newFullName by remember { mutableStateOf("") }
    var newMemberNumber by remember { mutableStateOf("") }
    var newRole by remember { mutableStateOf("member") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("USER MANAGEMENT", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { newEmail = ""; newPassword = ""; newFullName = ""; newMemberNumber = "MEM${System.currentTimeMillis() % 10000}"; newRole = "member"; showCreateDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) { Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("ADD", fontSize = 12.sp) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (users.isEmpty()) { EmptyBox("👤", "No Users", "Create your first user") }
        else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(users) { u ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(44.dp).background(when (u.role) { "admin" -> Color(0xFFFF6B6B); "moderator" -> Color(0xFFFF9800); else -> Color(0xFF007236) }, RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) { Text(u.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold) }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(u.fullName, color = Color.White, fontWeight = FontWeight.SemiBold)
                                    Text(u.email, color = Color(0xFF888888), fontSize = 12.sp)
                                }
                                Badge(containerColor = when (u.role) { "admin" -> Color(0xFFFF6B6B); "moderator" -> Color(0xFFFF9800); else -> Color(0xFF007236) }) { Text(u.role.replaceFirstChar { it.uppercase() }, fontSize = 10.sp) }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { selectedUser = u; showRoleDialog = true }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Security, null, modifier = Modifier.size(14.dp), tint = Color(0xFF90EE90)); Spacer(modifier = Modifier.width(4.dp)); Text("ROLE", fontSize = 11.sp, color = Color(0xFF90EE90)) }
                                OutlinedButton(onClick = { selectedUser = u; showDeleteDialog = true }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp), enabled = u.email != "admin@kdfgc.org") { Icon(Icons.Default.Delete, null, modifier = Modifier.size(14.dp), tint = if (u.email != "admin@kdfgc.org") Color(0xFFFF6B6B) else Color(0xFF444444)); Spacer(modifier = Modifier.width(4.dp)); Text("DELETE", fontSize = 11.sp, color = if (u.email != "admin@kdfgc.org") Color(0xFFFF6B6B) else Color(0xFF444444)) }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(onDismissRequest = { showCreateDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Create New User", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = newFullName, onValueChange = { newFullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newEmail, onValueChange = { newEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newMemberNumber, onValueChange = { newMemberNumber = it }, label = { Text("Member Number") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Role", color = Color(0xFF888888), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("member", "moderator", "admin").forEach { role ->
                            FilterChip(selected = newRole == role, onClick = { newRole = role }, label = { Text(role.replaceFirstChar { it.uppercase() }) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = when (role) { "admin" -> Color(0xFFFF6B6B); "moderator" -> Color(0xFFFF9800); else -> Color(0xFF007236) }))
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = { vm.createUser(newEmail, newPassword, newFullName, newMemberNumber, newRole, newRole == "admin"); showCreateDialog = false }, enabled = newEmail.isNotBlank() && newPassword.isNotBlank() && newFullName.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("CREATE") } },
            dismissButton = { OutlinedButton(onClick = { showCreateDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showRoleDialog && selectedUser != null) {
        AlertDialog(onDismissRequest = { showRoleDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Change Role: ${selectedUser!!.fullName}", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Current role: ${selectedUser!!.role}", color = Color(0xFF888888), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    listOf("member" to "Member - Basic access", "moderator" to "Moderator - Forum moderation", "admin" to "Admin - Full access").forEach { (role, desc) ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { vm.updateUserRole(selectedUser!!.id, role); showRoleDialog = false }, colors = CardDefaults.cardColors(containerColor = if (selectedUser!!.role == role) Color(0xFF333333) else Color(0xFF252525))) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedUser!!.role == role, onClick = { vm.updateUserRole(selectedUser!!.id, role); showRoleDialog = false })
                                Spacer(modifier = Modifier.width(8.dp))
                                Column { Text(role.replaceFirstChar { it.uppercase() }, color = Color.White, fontWeight = FontWeight.SemiBold); Text(desc, color = Color(0xFF888888), fontSize = 11.sp) }
                            }
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = { OutlinedButton(onClick = { showRoleDialog = false }) { Text("CLOSE", color = Color(0xFF888888)) } })
    }

    if (showDeleteDialog && selectedUser != null) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Delete User", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete ${selectedUser!!.fullName}? This cannot be undone.", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { vm.deleteUser(selectedUser!!); showDeleteDialog = false; selectedUser = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) { Text("DELETE") } },
            dismissButton = { OutlinedButton(onClick = { showDeleteDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}
@Composable
private fun ForumModerationContent(vm: UserViewModel) {
    val posts by vm.allForumPosts.collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<ForumPost?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("FORUM MODERATION", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (posts.isEmpty()) {
            EmptyBox("💬", "No Forum Posts", "Forum posts will appear here")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(posts) { p ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(36.dp).background(Color(0xFF007236), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
                                    Text(p.author.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(p.author, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(p.timestamp)), color = Color(0xFF888888), fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(p.content, color = Color(0xFFCCCCCC), fontSize = 13.sp, maxLines = 3)
                            if (!p.photoUri.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("📷 Has image attachment", color = Color(0xFF90EE90), fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(onClick = { selectedPost = p; showDeleteDialog = true }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                                Icon(Icons.Default.Delete, null, modifier = Modifier.size(14.dp), tint = Color(0xFFFF6B6B))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("DELETE POST", fontSize = 11.sp, color = Color(0xFFFF6B6B))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && selectedPost != null) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Delete Post", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Delete this post by ${selectedPost!!.author}? This action cannot be undone.", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { vm.deleteForumPost(selectedPost!!); showDeleteDialog = false; selectedPost = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) { Text("DELETE") } },
            dismissButton = { OutlinedButton(onClick = { showDeleteDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}
@Composable
private fun MemberDetailView(userId: Int, userViewModel: UserViewModel, onBack: () -> Unit) {
    var member by remember { mutableStateOf<User?>(null) }
    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var showMessageDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }
    var messageSubject by remember { mutableStateOf("") }
    var messageBody by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }
    var editPalNumber by remember { mutableStateOf("") }
    LaunchedEffect(userId) { member = userViewModel.getUserById(userId) }
    LaunchedEffect(member) { member?.let { editPhone = it.phone; editPalNumber = it.palNumber } }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }; Text("Member Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            if (member == null) { Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFF90EE90)) } }
            else {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(60.dp).background(Color(0xFF007236), RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) { Text(member!!.fullName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) }; Spacer(modifier = Modifier.width(16.dp)); Column { Text(member!!.fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text("Member #${member!!.memberNumber}", color = Color(0xFF90EE90)) } }
                        Spacer(modifier = Modifier.height(20.dp)); HorizontalDivider(color = Color(0xFF444444)); Spacer(modifier = Modifier.height(20.dp))
                        AdminInfoRow("Email", member!!.email); AdminInfoRow("Phone", member!!.phone.ifEmpty { "Not provided" }); AdminInfoRow("Member Type", member!!.membershipType); AdminInfoRow("PAL Number", member!!.palNumber.ifEmpty { "Not provided" }); AdminInfoRow("Role", member!!.role.replaceFirstChar { it.uppercase() })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = when (member!!.membershipType) { "Approved" -> Color(0xFF1B3D1B); "Rejected" -> Color(0xFF3D1B1B); else -> Color(0xFF3D3D1B) })) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(imageVector = when (member!!.membershipType) { "Approved" -> Icons.Default.CheckCircle; "Rejected" -> Icons.Default.Close; else -> Icons.Default.Schedule }, contentDescription = null, tint = when (member!!.membershipType) { "Approved" -> Color(0xFF4CAF50); "Rejected" -> Color(0xFFF44336); else -> Color(0xFFFFEB3B) }, modifier = Modifier.size(24.dp)); Spacer(modifier = Modifier.width(12.dp)); Text("Status: ${member!!.membershipType}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = { showMessageDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("MESSAGE") }; Button(onClick = { showEditDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("EDIT") } }
                if (member!!.membershipType == "Pending") { Spacer(modifier = Modifier.height(12.dp)); Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = { showApproveDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), modifier = Modifier.weight(1f)) { Text("APPROVE") }; Button(onClick = { showRejectDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), modifier = Modifier.weight(1f)) { Text("REJECT") } } }
            }
        }
    }

    if (showMessageDialog && member != null) {
        AlertDialog(onDismissRequest = { showMessageDialog = false }, containerColor = Color(0xFF252525), title = { Text("Send Message to ${member!!.fullName}", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Column { OutlinedTextField(value = messageSubject, onValueChange = { messageSubject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF2196F3), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true); Spacer(modifier = Modifier.height(12.dp)); OutlinedTextField(value = messageBody, onValueChange = { messageBody = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(150.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF2196F3), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White)) } },
            confirmButton = { Button(onClick = { userViewModel.sendAdminMessage(messageSubject, messageBody, listOf(member!!.id)); showMessageDialog = false; messageSubject = ""; messageBody = "" }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))) { Text("SEND") } },
            dismissButton = { OutlinedButton(onClick = { showMessageDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showEditDialog && member != null) {
        AlertDialog(onDismissRequest = { showEditDialog = false }, containerColor = Color(0xFF252525), title = { Text("Edit ${member!!.fullName}", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Column { OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true); Spacer(modifier = Modifier.height(12.dp)); OutlinedTextField(value = editPalNumber, onValueChange = { editPalNumber = it }, label = { Text("PAL Number") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true) } },
            confirmButton = { Button(onClick = { userViewModel.updateMemberInfo(member!!.id, editPhone, editPalNumber); member = member!!.copy(phone = editPhone, palNumber = editPalNumber); showEditDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("SAVE") } },
            dismissButton = { OutlinedButton(onClick = { showEditDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showApproveDialog) {
        AlertDialog(onDismissRequest = { showApproveDialog = false }, containerColor = Color(0xFF252525), title = { Text("Approve Member", color = Color.White) }, text = { Text("Approve ${member?.fullName}?", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { member?.let { userViewModel.updateMemberStatus(it.id, "Approved"); member = it.copy(membershipType = "Approved") }; showApproveDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) { Text("APPROVE") } },
            dismissButton = { OutlinedButton(onClick = { showApproveDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showRejectDialog) {
        AlertDialog(onDismissRequest = { showRejectDialog = false }, containerColor = Color(0xFF252525), title = { Text("Reject Member", color = Color.White) },
            text = { Column { Text("Reject ${member?.fullName}?", color = Color(0xFFAAAAAA)); Spacer(modifier = Modifier.height(12.dp)); OutlinedTextField(value = rejectReason, onValueChange = { rejectReason = it }, label = { Text("Reason") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFF44336), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White)) } },
            confirmButton = { Button(onClick = { member?.let { userViewModel.updateMemberStatus(it.id, "Rejected"); member = it.copy(membershipType = "Rejected") }; showRejectDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) { Text("REJECT") } },
            dismissButton = { OutlinedButton(onClick = { showRejectDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}

@Composable
private fun InvoiceDetailView(invoice: Invoice, userViewModel: UserViewModel, onBack: () -> Unit, onRefresh: () -> Unit) {
    var showRefundDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showFlagDialog by remember { mutableStateOf(false) }
    var refundReason by remember { mutableStateOf("") }
    var flagReason by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }; Text("Invoice Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (invoice.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Invoice #${invoice.id}", color = Color(0xFF90EE90), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        val statusColor = when (invoice.paymentStatus) { "Paid" -> Color(0xFF4CAF50); "Pending" -> Color(0xFFFF9800); "Refunded" -> Color(0xFF9C27B0); else -> Color(0xFF888888) }
                        Card(colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.2f))) { Text(invoice.paymentStatus, color = statusColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(20.dp)); HorizontalDivider(color = Color(0xFF444444)); Spacer(modifier = Modifier.height(20.dp))
                    AdminInfoRow("Item", invoice.itemName)
                    AdminInfoRow("Customer", invoice.userName)
                    AdminInfoRow("Quantity", invoice.quantity.toString())
                    AdminInfoRow("Price", invoice.price)
                    AdminInfoRow("Transaction ID", invoice.transactionId)
                    AdminInfoRow("Payment Method", invoice.paymentMethod)
                    AdminInfoRow("Date", java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(invoice.purchaseDate)))
                    if (invoice.notes.isNotBlank()) { Spacer(modifier = Modifier.height(12.dp)); Text("Notes:", color = Color(0xFF888888), fontSize = 12.sp); Text(invoice.notes, color = Color.White, fontSize = 14.sp) }
                    if (invoice.isFlagged) { Spacer(modifier = Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2020))) { Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Warning, null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Flagged: ${invoice.flagReason}", color = Color(0xFFFF6B6B), fontSize = 13.sp) } } }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("ACTIONS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (invoice.isFlagged) { Button(onClick = { userViewModel.unflagInvoice(invoice.id); Toast.makeText(context, "Invoice unflagged", Toast.LENGTH_SHORT).show(); onRefresh() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("UNFLAG") } }
                else { Button(onClick = { showFlagDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Flag, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("FLAG") } }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (invoice.paymentStatus == "Pending") { Button(onClick = { showReminderDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Notifications, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("REMIND") } }
                if (invoice.paymentStatus == "Paid") { Button(onClick = { showRefundDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)), modifier = Modifier.weight(1f)) { Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("REFUND") } }
            }
            if (invoice.paymentStatus == "Pending") {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { userViewModel.updateInvoiceStatus(invoice.id, "Paid"); Toast.makeText(context, "Marked as paid", Toast.LENGTH_SHORT).show(); onRefresh() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("MARK AS PAID") }
            }
        }
    }

    if (showRefundDialog) {
        AlertDialog(onDismissRequest = { showRefundDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Process Refund", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Refund ${invoice.price} to ${invoice.userName}?", color = Color(0xFFAAAAAA))
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = refundReason, onValueChange = { refundReason = it }, label = { Text("Refund Reason") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFE91E63), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("This will notify the member and update the invoice status.", color = Color(0xFF888888), fontSize = 12.sp)
                }
            },
            confirmButton = { Button(onClick = { userViewModel.processRefund(invoice, refundReason); Toast.makeText(context, "Refund processed", Toast.LENGTH_SHORT).show(); showRefundDialog = false; onRefresh() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))) { Text("PROCESS REFUND") } },
            dismissButton = { OutlinedButton(onClick = { showRefundDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showReminderDialog) {
        AlertDialog(onDismissRequest = { showReminderDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Send Payment Reminder", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Send a payment reminder to ${invoice.userName} for ${invoice.itemName} (${invoice.price})?", color = Color(0xFFAAAAAA)) },
            confirmButton = { Button(onClick = { userViewModel.sendInvoiceReminder(invoice); Toast.makeText(context, "Reminder sent", Toast.LENGTH_SHORT).show(); showReminderDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))) { Text("SEND REMINDER") } },
            dismissButton = { OutlinedButton(onClick = { showReminderDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }

    if (showFlagDialog) {
        AlertDialog(onDismissRequest = { showFlagDialog = false }, containerColor = Color(0xFF252525),
            title = { Text("Flag Invoice", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Flag this invoice for review?", color = Color(0xFFAAAAAA))
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = flagReason, onValueChange = { flagReason = it }, label = { Text("Reason") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF9800), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                }
            },
            confirmButton = { Button(onClick = { userViewModel.flagInvoice(invoice.id, flagReason.ifBlank { "Review needed" }); Toast.makeText(context, "Invoice flagged", Toast.LENGTH_SHORT).show(); showFlagDialog = false; onRefresh() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) { Text("FLAG") } },
            dismissButton = { OutlinedButton(onClick = { showFlagDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } })
    }
}
