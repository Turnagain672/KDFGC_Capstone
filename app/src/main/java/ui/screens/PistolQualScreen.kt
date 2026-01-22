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
import java.net.URLEncoder

@Composable
fun PistolQualScreen(navController: NavController) {
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
                text = "Pistol Qualification",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pistol Safety Qualification", color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Advanced qualification for members who want to improve their pistol skills. Covers marksmanship fundamentals, accuracy drills, and timed shooting exercises.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("COURSE DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Duration", "4 Hours")
            DetailRow("Cost", "$125")
            DetailRow("Prerequisites", "Handgun Safety completed")
            DetailRow("Ammo Required", "100 rounds (bring your own)")

            Spacer(modifier = Modifier.height(16.dp))

            Text("WHAT YOU'LL LEARN", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Proper grip and stance refinement")
            LearnItem("Sight alignment and sight picture")
            LearnItem("Trigger control and follow-through")
            LearnItem("Accuracy drills at various distances")
            LearnItem("Timed shooting exercises")
            LearnItem("Malfunction clearing")

            Spacer(modifier = Modifier.height(16.dp))

            Text("QUALIFICATION STANDARDS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    QualRow("7 yards", "10 rounds", "80% in scoring zone")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    QualRow("15 yards", "10 rounds", "70% in scoring zone")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    QualRow("25 yards", "10 rounds", "60% in scoring zone")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("UPCOMING DATES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DateCard("Saturday, February 8, 2026", "9:00 AM - 1:00 PM", "4 spots left")
            DateCard("Saturday, March 14, 2026", "9:00 AM - 1:00 PM", "8 spots left")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val courseName = URLEncoder.encode("Pistol Safety Qualification", "UTF-8")
                    navController.navigate("register/$courseName")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text("REGISTER NOW", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QualRow(distance: String, rounds: String, standard: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(distance, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(rounds, color = Color(0xFF888888), fontSize = 13.sp)
        Text(standard, color = Color(0xFF90EE90), fontSize = 13.sp)
    }
}