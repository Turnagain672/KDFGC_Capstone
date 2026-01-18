package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                TopNavItem("HOME")
                TopNavItem("JOIN US")
                TopNavItem("RENEW")
                TopNavItem("CONTACT")
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text("LOG IN", color = Color(0xFF007236), fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Kelowna & District Fish and Game Club",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ESTABLISHED 1904 • WILDLIFE CONSERVATION • SHOOTING SPORTS",
                    fontSize = 10.sp,
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
            NavTab("COURSES", false)
            NavTab("ABOUT", false)
            NavTab("STORE", false)
            NavTab("CONTACT", false)
        }

        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1474511320723-9a56873571b7?w=800",
                contentDescription = "Wildlife",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Dark overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66004D25))
            )
            // CTA Buttons
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CTAButton("BECOME A MEMBER", true)
                CTAButton("RENEW", true)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Range Updates Card
        InfoCard(title = "📢 RANGE UPDATES") {
            Text(
                text = "New Member Orientations are every second Saturday from 10:00 to 11:30. Registration required.",
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF444444))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Range Hours: Weekdays 9AM–8PM • Weekends 9AM–5PM",
                color = Color(0xFF90EE90),
                fontSize = 13.sp,
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
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
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun NavTab(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .then(
                if (selected) Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF007236))
                else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFFAAAAAA),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CTAButton(text: String, filled: Boolean) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filled) Color(0xFF007236) else Color.Transparent
        ),
        shape = RoundedCornerShape(25.dp),
        border = if (!filled) ButtonDefaults.outlinedButtonBorder else null,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
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
                    .padding(14.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
fun EventItem(title: String, time: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(text = time, color = Color(0xFF888888), fontSize = 12.sp)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun FeatureIcon(emoji: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2A2A2A))
            .padding(14.dp)
    ) {
        Text(text = emoji, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color(0xFF90EE90),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}