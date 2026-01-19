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
fun RifleRangeScreen(navController: NavController) {
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
                text = "Rifle Range",
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔫", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Outdoor Rifle Range", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("100, 200 & 300 yard distances", color = Color(0xFF888888), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("FACILITY DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Distances", "100, 200, 300 yards")
            DetailRow("Benches", "20 covered shooting positions")
            DetailRow("Targets", "Paper and steel available")
            DetailRow("Equipment", "Spotting scopes provided")

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
                    Text("(or dusk, whichever is earlier)", color = Color(0xFF888888), fontSize = 11.sp)
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    HoursRow("Saturday - Sunday", "9:00 AM - 5:00 PM")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("ALLOWED FIREARMS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Bolt-action rifles (all calibers)")
            LearnItem("Semi-automatic rifles (non-restricted)")
            LearnItem("Rimfire rifles")
            LearnItem("Shotguns (slugs only)")

            Spacer(modifier = Modifier.height(16.dp))

            Text("RANGE RULES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("• No rapid fire (1 shot per 2 seconds max)", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Cease fire at scheduled intervals", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Firearms must be unloaded during cease fire", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Eye and ear protection required", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• No tracer or incendiary ammunition", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252535))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("ℹ️ WEATHER DEPENDENT", color = Color(0xFF6B9FFF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Outdoor range may close due to weather conditions. Check the website or call ahead during winter months.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("palcourse") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text("BOOK PAL COURSE", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}