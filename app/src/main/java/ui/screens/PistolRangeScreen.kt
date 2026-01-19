package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PistolRangeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF007236))
                    )
                )
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Pistol Range",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hero
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎯", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Indoor Pistol Range", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Climate controlled • Year-round shooting", color = Color(0xFF888888), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("FACILITY DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Lanes", "10 shooting lanes")
            DetailRow("Distance", "25 yards (23 meters)")
            DetailRow("Target System", "Electronic retrieval")
            DetailRow("Ventilation", "State-of-the-art HVAC")
            DetailRow("Lighting", "Adjustable LED lighting")

            Spacer(modifier = Modifier.height(16.dp))

            Text("RANGE HOURS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    HoursRow("Monday - Friday", "9:00 AM - 8:00 PM")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    HoursRow("Saturday", "9:00 AM - 5:00 PM")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    HoursRow("Sunday", "10:00 AM - 4:00 PM")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("ALLOWED FIREARMS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Handguns (all calibers up to .50)")
            LearnItem("Pistol caliber carbines")
            LearnItem("Rimfire rifles (.22 LR)")

            Spacer(modifier = Modifier.height(16.dp))

            Text("REQUIREMENTS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("• Valid KDFGC membership", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• RPAL for restricted firearms", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Handgun Safety Qualification completed", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Eye and ear protection required", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("handgunsafety") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text("BOOK HANDGUN SAFETY COURSE", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HoursRow(day: String, hours: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day, color = Color.White, fontSize = 14.sp)
        Text(hours, color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}