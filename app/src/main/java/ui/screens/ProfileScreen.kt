package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstone2.data.AdminNotification
import com.example.capstone2.data.Invoice

@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val currentUser by userViewModel.currentUser.collectAsState()

    // Member inbox data
    val memberNotifications by userViewModel.getMemberNotifications(currentUser?.id ?: 0).collectAsState(initial = emptyList())
    val memberInvoices by userViewModel.getMemberInvoices(currentUser?.id ?: 0).collectAsState(initial = emptyList())
    val unreadCount = memberNotifications.count { !it.isRead }
    val pendingInvoices = memberInvoices.filter { it.paymentStatus == "Pending" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF003D1F),
                            Color(0xFF005A2B),
                            Color(0xFF007236)
                        )
                    )
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser?.fullName?.take(2)?.uppercase() ?: "JD",
                        color = Color(0xFF007236),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentUser?.fullName ?: "John Doe",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Member since 2023",
                    fontSize = 13.sp,
                    color = Color(0xFFCCCCCC)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (currentUser?.isAdmin == true) Color(0xFFFF6B6B) else Color(0xFF90EE90))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (currentUser?.isAdmin == true) "⚡ ADMIN" else "✓ ACTIVE MEMBER",
                        color = if (currentUser?.isAdmin == true) Color.White else Color(0xFF003D1F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==================== MEMBER INBOX SECTION ====================
        if (currentUser?.isAdmin != true) {
            MemberInboxSection(
                notifications = memberNotifications,
                invoices = memberInvoices,
                unreadCount = unreadCount,
                pendingInvoices = pendingInvoices,
                userViewModel = userViewModel,
                navController = navController
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { navController.navigate("myaccount") },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF007236))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "My Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View passes, cards & purchases",
                        color = Color(0xFFCCFFCC),
                        fontSize = 12.sp
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MEMBERSHIP DETAILS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF90EE90),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                MembershipRow("Member ID", currentUser?.memberNumber ?: "KDFGC-2023-0847")
                MembershipRow("Membership Type", "Full Adult")
                MembershipRow("Expiry Date", "December 31, 2026")
                MembershipRow("Range Certified", "Indoor, Outdoor, Archery")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileStatCard("Range Visits", "24", Icons.Default.LocationOn)
            ProfileStatCard("Events", "8", Icons.Default.Event)
            ProfileStatCard("Courses", "3", Icons.Default.School)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CERTIFICATIONS & QUALIFICATIONS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF90EE90),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                CertRow("PAL (Non-Restricted)", true)
                CertRow("RPAL (Restricted)", true)
                CertRow("New Member Orientation", true)
                CertRow("Handgun Safety Qualification", true)
                CertRow("Pistol Safety Qualification", false)
                CertRow("Handgun Basic Course", false)
                CertRow("Handgun Level 2 Course", false)
            }
        }

        // Admin Section - Only visible for admin users
        if (currentUser?.isAdmin == true) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ADMIN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminSettingsItem(Icons.Default.Dashboard, "Admin Panel", "Members, notifications & invoices") { navController.navigate("adminpanel") }
            AdminSettingsItem(Icons.Default.Store, "Store Management", "Manage products & pricing") { navController.navigate("admin") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SETTINGS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(Icons.Default.Person, "Edit Profile", "Update your information") { navController.navigate("editprofile") }
        SettingsItem(Icons.Default.Notifications, "Notifications", "Manage alerts") { navController.navigate("notifications") }
        SettingsItem(Icons.Default.Security, "Privacy & Security", "Account settings") { navController.navigate("privacy") }
        SettingsItem(Icons.Default.CreditCard, "Renew Membership", "Expires Dec 2026") { navController.navigate("renew") }
        SettingsItem(Icons.Default.Description, "Documents", "Certificates & waivers") { navController.navigate("documents") }
        SettingsItem(Icons.Default.Help, "Help & Support", "FAQs and contact") { navController.navigate("help") }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOG OUT", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ==================== MEMBER INBOX SECTION ====================

@Composable
fun MemberInboxSection(
    notifications: List<AdminNotification>,
    invoices: List<Invoice>,
    unreadCount: Int,
    pendingInvoices: List<Invoice>,
    userViewModel: UserViewModel,
    navController: NavController
) {
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showInvoicesDialog by remember { mutableStateOf(false) }
    var showRefundDialog by remember { mutableStateOf(false) }
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }
    var refundReason by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                    text = "MY INBOX",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3),
                    letterSpacing = 1.sp
                )
                if (unreadCount > 0) {
                    Badge(containerColor = Color(0xFFFF6B6B)) {
                        Text("$unreadCount NEW", fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notifications Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showNotificationsDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF2196F3), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Messages from Admin", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("${notifications.size} messages", color = Color(0xFF888888), fontSize = 12.sp)
                    }
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFFF6B6B), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$unreadCount", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF888888))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Invoices Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showInvoicesDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Receipt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("My Invoices", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("${invoices.size} total, ${pendingInvoices.size} pending", color = Color(0xFF888888), fontSize = 12.sp)
                    }
                    if (pendingInvoices.isNotEmpty()) {
                        Badge(containerColor = Color(0xFFFF9800)) { Text("${pendingInvoices.size}", fontSize = 10.sp) }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF888888))
                }
            }
        }
    }

    if (showNotificationsDialog) {
        AlertDialog(onDismissRequest = { showNotificationsDialog = false }, containerColor = Color(0xFF252525),
            title = { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Notifications, null, tint = Color(0xFF2196F3)); Spacer(modifier = Modifier.width(8.dp)); Text("Messages from Admin", color = Color.White, fontWeight = FontWeight.Bold) } },
            text = {
                if (notifications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("📭", fontSize = 32.sp); Spacer(modifier = Modifier.height(8.dp)); Text("No messages yet", color = Color(0xFF888888)) } }
                } else {
                    LazyColumn(modifier = Modifier.height(300.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(notifications) { n ->
                            Card(modifier = Modifier.fillMaxWidth().clickable { userViewModel.markNotificationAsRead(n.id) }, colors = CardDefaults.cardColors(containerColor = if (n.isRead) Color(0xFF333333) else Color(0xFF3A3A4A))) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(n.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp); if (!n.isRead) { Box(modifier = Modifier.size(8.dp).background(Color(0xFF2196F3), CircleShape)) } }
                                    Spacer(modifier = Modifier.height(4.dp)); Text(n.message, color = Color(0xFFAAAAAA), fontSize = 12.sp); Spacer(modifier = Modifier.height(4.dp))
                                    Text(java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(n.timestamp)), color = Color(0xFF666666), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = { showNotificationsDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("CLOSE") } }, dismissButton = {})
    }

    if (showInvoicesDialog) {
        AlertDialog(onDismissRequest = { showInvoicesDialog = false }, containerColor = Color(0xFF252525),
            title = { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Receipt, null, tint = Color(0xFF4CAF50)); Spacer(modifier = Modifier.width(8.dp)); Text("My Invoices", color = Color.White, fontWeight = FontWeight.Bold) } },
            text = {
                if (invoices.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("🧾", fontSize = 32.sp); Spacer(modifier = Modifier.height(8.dp)); Text("No invoices yet", color = Color(0xFF888888)) } }
                } else {
                    LazyColumn(modifier = Modifier.height(300.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(invoices) { inv ->
                            val statusColor = when (inv.paymentStatus) { "Paid" -> Color(0xFF4CAF50); "Pending" -> Color(0xFFFF9800); "Refunded" -> Color(0xFF9C27B0); "Refund Requested" -> Color(0xFFE91E63); else -> Color(0xFF888888) }
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(inv.itemName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f)); Text(inv.price, color = Color(0xFF90EE90), fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(inv.purchaseDate)), color = Color(0xFF888888), fontSize = 11.sp); Badge(containerColor = statusColor.copy(alpha = 0.2f)) { Text(inv.paymentStatus, color = statusColor, fontSize = 10.sp) } }
                                    if (inv.paymentStatus == "Paid") { Spacer(modifier = Modifier.height(8.dp)); OutlinedButton(onClick = { selectedInvoice = inv; showRefundDialog = true; showInvoicesDialog = false }, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(8.dp)) { Icon(Icons.Default.Refresh, null, modifier = Modifier.size(14.dp), tint = Color(0xFFE91E63)); Spacer(modifier = Modifier.width(4.dp)); Text("REQUEST REFUND", fontSize = 11.sp, color = Color(0xFFE91E63)) } }
                                    if (inv.paymentStatus == "Refund Requested") { Spacer(modifier = Modifier.height(8.dp)); Text("⏳ Awaiting admin approval", color = Color(0xFFE91E63), fontSize = 11.sp) }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = { showInvoicesDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("CLOSE") } }, dismissButton = {})
        if (showRefundDialog && selectedInvoice != null) {
            AlertDialog(onDismissRequest = { showRefundDialog = false }, containerColor = Color(0xFF252525),
                title = { Text("Request Refund", color = Color.White, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Item: ${selectedInvoice!!.itemName}", color = Color(0xFF90EE90), fontSize = 14.sp)
                        Text("Amount: ${selectedInvoice!!.price}", color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp)); Text("Please provide a reason:", color = Color(0xFF888888), fontSize = 12.sp); Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = refundReason, onValueChange = { refundReason = it }, label = { Text("Reason") }, modifier = Modifier.fillMaxWidth().height(120.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFE91E63), unfocusedBorderColor = Color(0xFF444444), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        Spacer(modifier = Modifier.height(8.dp)); Text("Your request will be sent to admin for approval.", color = Color(0xFF666666), fontSize = 11.sp)
                    }
                },
                confirmButton = { Button(onClick = { selectedInvoice?.let { inv -> userViewModel.requestRefund(inv, refundReason) }; showRefundDialog = false; refundReason = ""; selectedInvoice = null }, enabled = refundReason.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))) { Text("SUBMIT REQUEST") } },                dismissButton = { OutlinedButton(onClick = { showRefundDialog = false; refundReason = "" }) { Text("CANCEL", color = Color(0xFF888888)) } })
        }
    }
}

@Composable
fun MembershipRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFF888888), fontSize = 13.sp)
        Text(text = value, color = Color(0xFF90EE90), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun CertRow(name: String, completed: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null, tint = if (completed) Color(0xFF4CAF50) else Color(0xFF555555), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = name, color = if (completed) Color.White else Color(0xFF888888), fontSize = 13.sp)
        }
        Text(text = if (completed) "✓" else "—", color = if (completed) Color(0xFF4CAF50) else Color(0xFF555555), fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, icon: ImageVector) {
    Card(modifier = Modifier.width(105.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Color(0xFF007236), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color(0xFF90EE90), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = Color(0xFF888888), fontSize = 10.sp)
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF007236)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun AdminSettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A2525))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFFF6B6B)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
        }
    }
}