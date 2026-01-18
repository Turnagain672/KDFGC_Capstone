package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController

@Composable
fun DocumentsScreen(navController: NavController) {
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
            Text("Documents", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("CERTIFICATES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            DocumentItem("PAL Certificate", "Issued: March 15, 2023", Icons.Default.Verified)
            DocumentItem("RPAL Certificate", "Issued: March 15, 2023", Icons.Default.Verified)
            DocumentItem("Range Safety Certificate", "Issued: April 2, 2023", Icons.Default.Verified)

            Spacer(modifier = Modifier.height(24.dp))

            Text("WAIVERS & FORMS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            DocumentItem("Liability Waiver", "Signed: January 10, 2023", Icons.Default.Description)
            DocumentItem("Range Rules Agreement", "Signed: January 10, 2023", Icons.Default.Description)
            DocumentItem("Emergency Contact Form", "Updated: June 5, 2024", Icons.Default.Description)

            Spacer(modifier = Modifier.height(24.dp))

            Text("MEMBERSHIP", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            DocumentItem("Membership Card", "Valid until Dec 2026", Icons.Default.CreditCard)
            DocumentItem("Payment Receipt 2026", "Paid: January 5, 2026", Icons.Default.Receipt)
        }
    }
}

@Composable
fun DocumentItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Icon(Icons.Default.Download, contentDescription = "Download", tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
        }
    }
}