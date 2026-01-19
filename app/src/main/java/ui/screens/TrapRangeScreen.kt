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
fun TrapRangeScreen(navController: NavController) {
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
                text = "Trap Range",
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
                    Text("🥏", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Trap & Shotgun Sports", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Clay target shooting • All skill levels", color = Color(0xFF888888), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("FACILITY DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Fields", "5 trap fields")
            DetailRow("Throwers", "Automated clay throwers")
            DetailRow("Clays", "Available for purchase")
            DetailRow("Ammunition", "12 & 20 gauge available")

            Spacer(modifier = Modifier.height(16.dp))

            Text("RANGE HOURS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    HoursRow("Wednesday", "5:00 PM - Dusk")
                    Text("(Summer evenings)", color = Color(0xFF888888), fontSize = 11.sp)
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    HoursRow("Saturday", "9:00 AM - 3:00 PM")
                    Text("(Year-round)", color = Color(0xFF888888), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("PROGRAMS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Saturday Trapshooting (drop-in)")
            LearnItem("Wednesday Evening Trap (summer)")
            LearnItem("Beginner Shotgun Introduction")
            LearnItem("Youth Shotgun Program")
            LearnItem("Club Competitions")

            Spacer(modifier = Modifier.height(16.dp))

            Text("PRICING", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    PriceRow("Round of 25 clays", "$8")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    PriceRow("Box of shells (25)", "$12")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    PriceRow("Shotgun rental", "$20/session")
                }
            }

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
                    Text("• PAL for non-restricted firearms", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Eye and ear protection required", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Only target loads permitted (7.5, 8, 9 shot)", color = Color(0xFFCCCCCC), fontSize = 13.sp)
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

@Composable
fun PriceRow(item: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item, color = Color.White, fontSize = 14.sp)
        Text(price, color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}