package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
fun RPALCourseScreen(navController: NavController) {
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
                text = "RPAL Course",
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
                    Text("Canadian Restricted Firearms Safety Course", color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "The CRFSC is required to obtain your Restricted Possession and Acquisition License (RPAL) for restricted firearms such as handguns and AR-15 style rifles.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("COURSE DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Duration", "1 Day (8 hours)")
            DetailRow("Cost", "$100")
            DetailRow("Includes", "Manual, exam, certificate")
            DetailRow("Prerequisites", "PAL or CFSC completed")
            DetailRow("Min Age", "18 years")

            Spacer(modifier = Modifier.height(16.dp))

            Text("WHAT YOU'LL LEARN", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Restricted firearm safety rules")
            LearnItem("Handgun operation and handling")
            LearnItem("Revolvers and semi-automatic pistols")
            LearnItem("Safe storage requirements for restricted firearms")
            LearnItem("Transportation regulations (ATT)")
            LearnItem("Practical handling demonstration")

            Spacer(modifier = Modifier.height(16.dp))

            Text("UPCOMING DATES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DateCard("Sunday, February 9, 2026", "9:00 AM - 5:00 PM", "6 spots left")
            DateCard("Sunday, February 23, 2026", "9:00 AM - 5:00 PM", "10 spots left")
            DateCard("Sunday, March 9, 2026", "9:00 AM - 5:00 PM", "12 spots left")

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3D2525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("⚠️ PREREQUISITE REQUIRED", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "You must have completed the PAL (CFSC) course before taking this course.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { },
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