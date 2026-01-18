package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventsScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF007236))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "📅 UPCOMING EVENTS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Club activities and training sessions",
                    fontSize = 12.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Events List
        EventCard(
            title = "Precision Pistol BCTSA",
            date = "January 14, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Indoor Range",
            description = "Monthly precision pistol competition. All skill levels welcome."
        )

        EventCard(
            title = "New Member Orientation",
            date = "January 18, 2026",
            time = "10:00 AM - 11:30 AM",
            location = "Clubhouse",
            description = "Mandatory orientation for new members. Learn range rules and safety."
        )

        EventCard(
            title = "Handgun Safety Course",
            date = "January 20, 2026",
            time = "9:00 AM - 4:00 PM",
            location = "Indoor Range",
            description = "Comprehensive handgun safety training. Certification provided."
        )

        EventCard(
            title = "Youth Core Program",
            date = "January 25, 2026",
            time = "10:00 AM - 12:00 PM",
            location = "Outdoor Range",
            description = "Youth shooting sports introduction. Ages 12-17. Parent must attend."
        )

        EventCard(
            title = "RPAL Course",
            date = "February 1, 2026",
            time = "8:00 AM - 5:00 PM",
            location = "Clubhouse",
            description = "Restricted Possession and Acquisition License course."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun EventCard(
    title: String,
    date: String,
    time: String,
    location: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column {
            // Card Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007236))
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Card Content
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF90EE90),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = date, color = Color.White, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF90EE90),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = time, color = Color(0xFFAAAAAA), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF90EE90),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = location, color = Color(0xFFAAAAAA), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFF333333))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = description,
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text("REGISTER", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}