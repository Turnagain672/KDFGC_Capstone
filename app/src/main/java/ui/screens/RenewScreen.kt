package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder

@Composable
fun RenewScreen(navController: NavController) {
    val membershipDaysLeft = 347
    val palDaysLeft = 120
    val rpalDaysLeft = 45
    val handgunSafetyDaysLeft = 14
    val orientationDaysLeft = 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(rememberScrollState())
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
            Text("Renew Membership", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Membership Status Card
            ExpiryCard(
                title = "MEMBERSHIP",
                expiryDate = "December 31, 2026",
                daysLeft = membershipDaysLeft
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications for expiring items
            if (handgunSafetyDaysLeft <= 30) {
                ExpiryWarningCard(
                    title = "Handgun Safety Qualification",
                    daysLeft = handgunSafetyDaysLeft
                )
            }
            if (orientationDaysLeft <= 30) {
                ExpiryWarningCard(
                    title = "New Member Orientation",
                    daysLeft = orientationDaysLeft
                )
            }
            if (rpalDaysLeft <= 30) {
                ExpiryWarningCard(
                    title = "RPAL Certificate",
                    daysLeft = rpalDaysLeft
                )
            }
            if (palDaysLeft <= 30) {
                ExpiryWarningCard(
                    title = "PAL Certificate",
                    daysLeft = palDaysLeft
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("MEMBERSHIP RENEWAL", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            RenewalOptionClickable("1 Year Membership", "150.00", "Best for casual shooters", navController)
            RenewalOptionClickable("2 Year Membership", "270.00", "Save \$30", navController)
            RenewalOptionClickable("5 Year Membership", "625.00", "Save \$125 - Best Value", navController)

            Spacer(modifier = Modifier.height(24.dp))

            Text("CERTIFICATES & QUALIFICATIONS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            CertificateStatusCard("PAL (Non-Restricted)", "March 15, 2026", palDaysLeft, navController)
            CertificateStatusCard("RPAL (Restricted)", "February 1, 2026", rpalDaysLeft, navController)
            CertificateStatusCard("Handgun Safety Qualification", "January 30, 2026", handgunSafetyDaysLeft, navController)
            CertificateStatusCard("New Member Orientation", "January 21, 2026", orientationDaysLeft, navController)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Early renewal extends your current membership. No days lost!",
                color = Color(0xFF888888),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("BACK TO PROFILE", color = Color(0xFF888888), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ExpiryCard(title: String, expiryDate: String, daysLeft: Int) {
    val statusColor = when {
        daysLeft <= 7 -> Color(0xFFFF5252)
        daysLeft <= 14 -> Color(0xFFFF9800)
        daysLeft <= 30 -> Color(0xFFFFEB3B)
        else -> Color(0xFF4CAF50)
    }

    val statusText = when {
        daysLeft <= 7 -> "EXPIRES SOON!"
        daysLeft <= 14 -> "2 WEEKS LEFT"
        daysLeft <= 30 -> "1 MONTH LEFT"
        else -> "ACTIVE"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (daysLeft <= 14) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(statusText, color = statusColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Expires $expiryDate", color = Color.White, fontSize = 16.sp)
            Text("$daysLeft days remaining", color = Color(0xFF888888), fontSize = 13.sp)
        }
    }
}

@Composable
fun ExpiryWarningCard(title: String, daysLeft: Int) {
    val warningColor = when {
        daysLeft <= 7 -> Color(0xFFFF5252)
        daysLeft <= 14 -> Color(0xFFFF9800)
        else -> Color(0xFFFFEB3B)
    }

    val warningText = when {
        daysLeft <= 1 -> "EXPIRES TODAY!"
        daysLeft <= 7 -> "EXPIRES IN $daysLeft DAYS!"
        daysLeft <= 14 -> "2 WEEKS NOTICE"
        else -> "1 MONTH NOTICE"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = warningColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = warningColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(warningText, color = warningColor, fontSize = 12.sp)
            }
            Text("$daysLeft days", color = warningColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RenewalOptionClickable(title: String, price: String, description: String, navController: NavController) {
    val encodedTitle = URLEncoder.encode(title, "UTF-8")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate("checkout/$encodedTitle/$price") },
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
                Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(description, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$$price", color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Tap to renew →", color = Color(0xFF007236), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun CertificateStatusCard(title: String, expiryDate: String, daysLeft: Int, navController: NavController) {
    val statusColor = when {
        daysLeft <= 7 -> Color(0xFFFF5252)
        daysLeft <= 14 -> Color(0xFFFF9800)
        daysLeft <= 30 -> Color(0xFFFFEB3B)
        else -> Color(0xFF4CAF50)
    }

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
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Expires: $expiryDate", color = Color(0xFF888888), fontSize = 11.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$daysLeft days", color = statusColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (daysLeft <= 30) {
                    Text("Renew now", color = statusColor, fontSize = 10.sp)
                }
            }
        }
    }
}