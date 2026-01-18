package com.example.capstone2.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HelpScreen(navController: NavController) {
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
            Text("Help & Support", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("CONTACT US", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            ContactItem(Icons.Default.Phone, "Phone", "250-764-7558")
            ContactItem(Icons.Default.Email, "Email", "info@kdfgc.org")
            ContactItem(Icons.Default.LocationOn, "Address", "4041 Casorso Rd, Kelowna, BC")

            Spacer(modifier = Modifier.height(24.dp))

            Text("OFFICE HOURS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Tuesday & Thursday", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text("11:00 AM - 4:00 PM", color = Color(0xFF888888), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Saturday", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text("9:00 AM - 3:00 PM", color = Color(0xFF888888), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("FAQ", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            FaqItem("How do I book a range lane?", "Use the Events tab to view available times and register for a session.")
            FaqItem("What are the range rules?", "All members must complete orientation. Eye and ear protection required. No rapid fire without approval.")
            FaqItem("How do I renew my membership?", "Go to Profile → Renew Membership to see options and pay online.")
            FaqItem("Can I bring a guest?", "Yes, members can bring guests. Guests must sign a waiver and be supervised at all times.")
        }
    }
}

@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF007236), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(label, color = Color(0xFF888888), fontSize = 11.sp)
                Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF888888)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer, color = Color(0xFF888888), fontSize = 13.sp)
            }
        }
    }
}