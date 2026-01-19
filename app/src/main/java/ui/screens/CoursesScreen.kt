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
fun CoursesScreen(navController: NavController) {
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
                text = "Courses & Training",
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
            Text("FIREARMS LICENSING", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            CourseCard(
                title = "PAL Course",
                subtitle = "Possession & Acquisition License",
                duration = "1 Day",
                price = "$150",
                description = "Canadian Firearms Safety Course for non-restricted firearms."
            )

            CourseCard(
                title = "RPAL Course",
                subtitle = "Restricted PAL",
                duration = "1 Day",
                price = "$100",
                description = "Canadian Restricted Firearms Safety Course. PAL required."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("HANDGUN TRAINING", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            CourseCard(
                title = "Handgun Safety Qualification",
                subtitle = "Required for range use",
                duration = "2 Hours",
                price = "$50",
                description = "Basic handgun safety and range procedures. Required for all new members."
            )

            CourseCard(
                title = "Pistol Basic Course",
                subtitle = "Beginner level",
                duration = "4 Hours",
                price = "$125",
                description = "Fundamentals of pistol shooting, stance, grip, and accuracy."
            )

            CourseCard(
                title = "Pistol Level 2",
                subtitle = "Intermediate",
                duration = "4 Hours",
                price = "$150",
                description = "Advanced techniques, drawing from holster, multiple targets."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("OTHER COURSES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            CourseCard(
                title = "New Member Orientation",
                subtitle = "Required for all members",
                duration = "2 Hours",
                price = "Free",
                description = "Range rules, safety procedures, and facility tour. Every second Saturday."
            )

            CourseCard(
                title = "Archery Introduction",
                subtitle = "All ages welcome",
                duration = "3 Hours",
                price = "$75",
                description = "Learn the basics of archery. Equipment provided."
            )

            CourseCard(
                title = "Youth Firearms Safety",
                subtitle = "Ages 12-18",
                duration = "1 Day",
                price = "$100",
                description = "Safe firearm handling for young shooters. Parent must attend."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CourseCard(title: String, subtitle: String, duration: String, price: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = Color(0xFF888888), fontSize = 12.sp)
                }
                Text(price, color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(description, color = Color(0xFFCCCCCC), fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Duration: $duration", color = Color(0xFF888888), fontSize = 12.sp)
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("REGISTER", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}