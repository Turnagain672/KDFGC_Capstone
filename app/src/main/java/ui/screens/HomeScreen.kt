package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Top Nav Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF003D1F))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TopNavItem("HOME")
                TopNavItem("JOIN US")
                TopNavItem("RENEW")
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("LOG IN", color = Color(0xFF007236), fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        // Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF003D1F),
                            Color(0xFF005A2B),
                            Color(0xFF007236)
                        )
                    )
                )
                .padding(vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Kelowna & District\nFish and Game Club",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ESTABLISHED 1904 • WILDLIFE CONSERVATION",
                    fontSize = 9.sp,
                    color = Color(0xFFCCCCCC),
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Main Nav Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2A2A))
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            NavTab("HOME", true)
            NavTab("EVENTS", false)
            NavTab("RANGES", false)
            NavTab("CLUBS", false)
            NavTab("ABOUT", false)
        }

        // Hero Section (gradient instead of image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF004D25),
                            Color(0xFF003318),
                            Color(0xFF1A1A1A)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🦌", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CTAButton("BECOME A MEMBER")
                    CTAButton("RENEW")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Range Updates Card
        InfoCard(title = "📢 RANGE UPDATES") {
            Text(
                text = "New Member Orientations every second Saturday 10:00-11:30. Registration required.",
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF444444))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Hours: Weekdays 9AM–8PM • Weekends 9AM–5PM",
                color = Color(0xFF90EE90),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Upcoming Events Card
        InfoCard(title = "📅 UPCOMING EVENTS") {
            EventItem("Precision Pistol BCTSA", "Jan 14 • 6:00 PM")
            EventItem("New Member Orientation", "Jan 18 • 10:00 AM")
            EventItem("Handgun Safety Course", "Jan 20 • 9:00 AM")
        }

        // Facilities
        Text(
            text = "OUR FACILITIES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureIcon("🎯", "Indoor")
            FeatureIcon("🔫", "Outdoor")
            FeatureIcon("🏹", "Archery")
            FeatureIcon("🎓", "Training")
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TopNavItem(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun NavTab(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .then(
                if (selected) Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF007236))
                else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFFAAAAAA),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CTAButton(text: String) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF007236), Color(0xFF009944))
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column(modifier = Modifier.padding(14.dp), content = content)
        }
    }
}

@Composable
fun EventItem(title: String, time: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(text = time, color = Color(0xFF888888), fontSize = 11.sp)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun FeatureIcon(emoji: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2A2A2A))
            .padding(12.dp)
    ) {
        Text(text = emoji, fontSize = 26.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color(0xFF90EE90),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}