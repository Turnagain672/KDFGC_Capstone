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
import java.net.URLEncoder

@Composable
fun PALCourseScreen(navController: NavController) {
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
                text = "PAL Course",
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
                    Text("Canadian Firearms Safety Course", color = Color(0xFF90EE90), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "The CFSC is the mandatory course required to obtain your Possession and Acquisition License (PAL) for non-restricted firearms in Canada.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("COURSE DETAILS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Duration", "1 Day (8 hours)")
            DetailRow("Cost", "$150")
            DetailRow("Includes", "Manual, exam, certificate")
            DetailRow("Prerequisites", "None")
            DetailRow("Min Age", "12 years (with parent consent)")

            Spacer(modifier = Modifier.height(16.dp))

            Text("WHAT YOU'LL LEARN", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LearnItem("Firearm safety rules and ACTS/PROVE")
            LearnItem("Ammunition types and identification")
            LearnItem("Rifle and shotgun operation")
            LearnItem("Safe storage and transportation")
            LearnItem("Canadian firearms laws and regulations")
            LearnItem("Practical handling demonstration")

            Spacer(modifier = Modifier.height(16.dp))

            Text("UPCOMING DATES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            DateCard("Saturday, February 8, 2026", "9:00 AM - 5:00 PM", "4 spots left")
            DateCard("Saturday, February 22, 2026", "9:00 AM - 5:00 PM", "8 spots left")
            DateCard("Saturday, March 8, 2026", "9:00 AM - 5:00 PM", "12 spots left")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val courseName = URLEncoder.encode("PAL Course - Canadian Firearms Safety Course", "UTF-8")
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
fun DetailRow(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color(0xFF888888), fontSize = 14.sp)
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LearnItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = Color(0xFFCCCCCC), fontSize = 14.sp)
    }
}

@Composable
fun DateCard(date: String, time: String, spots: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(date, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(time, color = Color(0xFF888888), fontSize = 12.sp)
            }
            Text(spots, color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}