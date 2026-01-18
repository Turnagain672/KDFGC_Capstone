package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NotificationsScreen(navController: NavController) {
    var eventReminders by remember { mutableStateOf(true) }
    var rangeUpdates by remember { mutableStateOf(true) }
    var membershipAlerts by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(false) }
    var smsNotifications by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF007236))
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("PUSH NOTIFICATIONS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            NotificationToggle("Event Reminders", "Get notified about upcoming events", eventReminders) { eventReminders = it }
            NotificationToggle("Range Updates", "Closures and schedule changes", rangeUpdates) { rangeUpdates = it }
            NotificationToggle("Membership Alerts", "Renewal and expiry reminders", membershipAlerts) { membershipAlerts = it }

            Spacer(modifier = Modifier.height(24.dp))

            Text("OTHER NOTIFICATIONS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            NotificationToggle("Email Notifications", "Receive updates via email", emailNotifications) { emailNotifications = it }
            NotificationToggle("SMS Notifications", "Receive text messages", smsNotifications) { smsNotifications = it }
        }
    }
}

@Composable
fun NotificationToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF007236),
                    uncheckedThumbColor = Color(0xFF888888),
                    uncheckedTrackColor = Color(0xFF444444)
                )
            )
        }
    }
}