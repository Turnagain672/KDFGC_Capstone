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
fun RangesScreen(navController: NavController) {
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
                text = "Our Ranges",
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
            RangeCard(
                title = "Pistol Range",
                icon = "🎯",
                description = "Indoor 25-yard range with 10 lanes. Climate controlled for year-round shooting.",
                hours = "Weekdays: 9 AM - 8 PM\nWeekends: 9 AM - 5 PM",
                features = listOf("10 shooting lanes", "25-yard distance", "Climate controlled", "Target retrieval system")
            )

            Spacer(modifier = Modifier.height(12.dp))

            RangeCard(
                title = "Rifle Range",
                icon = "🔫",
                description = "Outdoor range with distances from 100 to 300 yards. Covered shooting positions.",
                hours = "Weekdays: 9 AM - Dusk\nWeekends: 9 AM - 5 PM",
                features = listOf("100, 200, 300 yard lines", "Covered benches", "Steel targets available", "Spotting scopes provided")
            )

            Spacer(modifier = Modifier.height(12.dp))

            RangeCard(
                title = "Archery Range",
                icon = "🏹",
                description = "Indoor and outdoor archery facilities for all skill levels.",
                hours = "Weekdays: 9 AM - 8 PM\nWeekends: 9 AM - 5 PM",
                features = listOf("Indoor 20-yard range", "Outdoor 3D course", "Target butts provided", "Bow rentals available")
            )

            Spacer(modifier = Modifier.height(12.dp))

            RangeCard(
                title = "Trap Range",
                icon = "🥏",
                description = "Shotgun sports facility with automated trap machines.",
                hours = "Saturdays: 9 AM - 3 PM\nWednesdays: 5 PM - Dusk",
                features = listOf("5 trap fields", "Automated throwers", "Ammunition available", "Shotgun rentals")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("RANGE RULES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Eye and ear protection required", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• No rapid fire without approval", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Follow all Range Officer commands", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Firearms must be unloaded when not on firing line", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RangeCard(title: String, icon: String, description: String, hours: String, features: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(description, color = Color(0xFFCCCCCC), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Text("HOURS", color = Color(0xFF90EE90), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(hours, color = Color(0xFF888888), fontSize = 12.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Text("FEATURES", color = Color(0xFF90EE90), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            features.forEach { feature ->
                Text("• $feature", color = Color(0xFF888888), fontSize = 12.sp)
            }
        }
    }
}