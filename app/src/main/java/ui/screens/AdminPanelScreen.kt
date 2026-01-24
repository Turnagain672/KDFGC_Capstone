package com.example.capstone2.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.AdminNotification
import com.example.capstone2.data.Course
import com.example.capstone2.data.Invoice
import com.example.capstone2.data.User

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
    val tabs = listOf("Notifications", "Members", "Invoices", "Courses")
    val notifications by userViewModel.activeNotifications.collectAsState(initial = emptyList())
    val unreadCount by userViewModel.unreadCount.collectAsState(initial = 0)
    var selectedMemberId by remember { mutableStateOf<Int?>(null) }
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }
    var showExpiryMemberList by remember { mutableStateOf(false) }
    var selectedExpiryMember by remember { mutableStateOf<User?>(null) }
    var expiryMessageSubject by remember { mutableStateOf("Certification Expiry Reminder") }
    var expiryMessageBody by remember { mutableStateOf("Dear Member,\n\nYour certification is expiring soon. Please rebook your course or renew your license to maintain your range access.\n\nContact us if you have any questions.\n\nBest regards,\nKDFGC Admin Team") }
    val allMembers by userViewModel.searchResults.collectAsState()

    LaunchedEffect(showExpiryMemberList) { if (showExpiryMemberList) userViewModel.getAllMembers() }

    BackHandler(enabled = selectedMemberId != null || selectedInvoice != null || selectedExpiryMember != null) {
        when {
            selectedExpiryMember != null -> selectedExpiryMember = null
            selectedMemberId != null -> selectedMemberId = null
            selectedInvoice != null -> selectedInvoice = null
        }
    }

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
    val icon = when (n.type) { "document" -> Icons.Default.Description; "purchase" -> Icons.Default.ShoppingCart; "member" -> Icons.Default.PersonAdd; "alert" -> Icons.Default.Warning; else -> Icons.Default.Notifications }
    val iconColor = when (n.type) { "document" -> Color(0xFF2196F3); "purchase" -> Color(0xFF4CAF50); "member" -> Color(0xFF9C27B0); "alert" -> Color(0xFFFF9800); else -> Color(0xFF888888) }
    Card(modifier = Modifier.fillMaxWidth().clickable { vm.markNotificationAsRead(n.id); when { n.type == "alert" && n.title.contains("Expiry") -> onExpiryAlert(); n.relatedUserId != null -> onMember(n.relatedUserId); else -> onInvoice() } }, colors = CardDefaults.cardColors(containerColor = if (n.isRead) Color(0xFF252525) else Color(0xFF2A2A2A))) {
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
    val invoices by vm.allInvoices.collectAsState(initial = emptyList())
    var flaggedOnly by remember { mutableStateOf(false) }
    val flagged by vm.flaggedInvoices.collectAsState(initial = emptyList())
    val list = if (flaggedOnly) flagged else invoices
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("INVOICES", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold); Row(verticalAlignment = Alignment.CenterVertically) { Text("Flagged Only", color = Color(0xFF888888), fontSize = 12.sp); Switch(checked = flaggedOnly, onCheckedChange = { flaggedOnly = it }) } }
        Spacer(modifier = Modifier.height(12.dp))
        if (list.isEmpty()) { EmptyBox("🧾", "No Invoices", "No invoices yet") }
        else { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(list) { i -> InvoiceItem(i) { onInvoice(i) } } } }
    }
}

@Composable
private fun InvoiceItem(i: Invoice, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = if (i.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text(i.itemName, color = Color.White, fontWeight = FontWeight.SemiBold); Text(i.userName, color = Color(0xFF888888), fontSize = 12.sp) }; Text(i.price, color = Color(0xFF90EE90), fontWeight = FontWeight.Bold) }
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
                        AdminInfoRow("Email", member!!.email); AdminInfoRow("Phone", member!!.phone.ifEmpty { "Not provided" }); AdminInfoRow("Member Type", member!!.membershipType); AdminInfoRow("PAL Number", member!!.palNumber.ifEmpty { "Not provided" }); AdminInfoRow("Admin", if (member!!.isAdmin) "Yes" else "No")
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
private fun InvoiceDetailView(invoice: Invoice, userViewModel: UserViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }; Text("Invoice Details", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
        Column(modifier = Modifier.padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (invoice.isFlagged) Color(0xFF3A2525) else Color(0xFF252525))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Invoice #${invoice.id}", color = Color(0xFF90EE90), fontWeight = FontWeight.Bold); Text(invoice.paymentStatus, color = Color.White) }
                    Spacer(modifier = Modifier.height(20.dp)); HorizontalDivider(color = Color(0xFF444444)); Spacer(modifier = Modifier.height(20.dp))
                    AdminInfoRow("Item", invoice.itemName); AdminInfoRow("Customer", invoice.userName); AdminInfoRow("Quantity", invoice.quantity.toString()); AdminInfoRow("Price", invoice.price); AdminInfoRow("Transaction ID", invoice.transactionId); AdminInfoRow("Payment Method", invoice.paymentMethod)
                    if (invoice.isFlagged) { Spacer(modifier = Modifier.height(16.dp)); Text("⚠ Flagged: ${invoice.flagReason}", color = Color(0xFFFF6B6B)) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (invoice.isFlagged) { Button(onClick = { userViewModel.unflagInvoice(invoice.id); onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), modifier = Modifier.weight(1f)) { Text("UNFLAG") } }
                else { Button(onClick = { userViewModel.flagInvoice(invoice.id, "Review needed"); onBack() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)), modifier = Modifier.weight(1f)) { Text("FLAG") } }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), modifier = Modifier.weight(1f)) { Text("REFUND") }
            }
        }
    }
}