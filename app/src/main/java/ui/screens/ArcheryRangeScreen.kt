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
fun ArcheryRangeScreen(navController: NavController) {
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
                text = "Archery Range",
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
                    Text("🏹", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Archery Facilities", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Indoor & outdoor ranges • All skill levels", color = Color(0xFF888888), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("INDOOR RANGE", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Distance", "20 yards (18 meters)")
            DetailRow("Lanes", "12 shooting lanes")
            DetailRow("Climate", "Heated, year-round")
            DetailRow("Targets", "Paper targets provided")

            Spacer(modifier = Modifier.height(16.dp))

            Text("OUTDOOR RANGE", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Distances", "20 to 70 meters")
            DetailRow("3D Course", "30 target walk-through")
            DetailRow("Field Course", "Varied terrain and distances")

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
                    HoursRow("Saturday - Sunday", "9:00 AM - 5:00 PM")
                    HorizontalDivider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))
                    Text("Sunday Night League", color = Color(0xFF90EE90), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Sundays 6:00 PM - 9:00 PM", color = Color(0xFF888888), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("PROGRAMS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Sunday Night Archery League")
            LearnItem("Open Archery (drop-in)")
            LearnItem("Junior Archery (BCAA JOP)")
            LearnItem("Adult Archery (BCAA)")
            LearnItem("Beginner Introduction Course")

            Spacer(modifier = Modifier.height(16.dp))

            Text("EQUIPMENT", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("• Recurve bows available for rent", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Compound bows available for rent", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Arrows included with rental", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Targets and target faces provided", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Arm guards and finger tabs available", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("events") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text("VIEW ARCHERY EVENTS", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}