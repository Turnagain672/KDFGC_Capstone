package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
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
                TopNavItem("HOME") { navController.navigate("home") }
                TopNavItem("JOIN US") { navController.navigate("join") }
                TopNavItem("RENEW") { navController.navigate("renew") }
            }
            Button(
                onClick = { navController.navigate("login") },
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
            NavTab("HOME", true) { }
            NavTab("EVENTS", false) { navController.navigate("events") }
            NavTab("RANGES", false) { navController.navigate("ranges") }
            NavTab("COURSES", false) { navController.navigate("courses") }
            NavTab("ABOUT", false) { navController.navigate("about") }
        }

        // Hero Section
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
                    CTAButton("BECOME A MEMBER") { navController.navigate("join") }
                    CTAButton("RENEW") { navController.navigate("renew") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Range Hours Card
        InfoCard(title = "🕐 RANGE HOURS") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Outdoor Range", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Weekdays: 9 AM - 8 PM", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                    Text("(or dusk, if earlier)", color = Color(0xFF888888), fontSize = 10.sp)
                    Text("Weekends: 9 AM - 5 PM", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                }
                Column {
                    Text("Office Hours", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Tue & Thu: 11 AM - 4 PM", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                    Text("Saturday: 9 AM - 3 PM", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                }
            }
        }

        // Range Updates Card
        InfoCard(title = "📢 RANGE UPDATES") {
            Text(
                text = "New Member Orientations are every second Saturday from 10:00 to 11:30. Please register online. This training is a club and legal requirement for new members prior to shooting unsupervised at the range.",
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        // Upcoming Events Card
        InfoCard(title = "📅 UPCOMING EVENTS") {
            EventItem("Sunday Night Archery League", "Session 3 of 13 • Indoor Range")
            EventItem("Open Archery", "Session 9 of 15 • Archery Range")
            EventItem("Junior Archery (BCAA JOP)", "Session 15 of 25 • Archery Range")
            EventItem("Adult Archery (BCAA)", "Session 15 of 25 • Archery Range")
            EventItem("Trapshooting", "Saturdays • Trap Range")
            EventItem("Precision Rifle", "Outdoor Range")
        }

        // Featured News
        InfoCard(title = "📰 FEATURED NEWS") {
            Text(
                text = "KDFGC Pistol Members, Recreational Pistol",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Improve Your Pistol Skills",
                color = Color(0xFF90EE90),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Youth Core Program This Winter",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Programs available for youth members",
                color = Color(0xFF888888),
                fontSize = 12.sp
            )
        }

        // Courses
        Text(
            text = "COURSES & TRAINING",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CourseChip("PAL Course") { navController.navigate("palcourse") }
            CourseChip("RPAL Course") { navController.navigate("rpalcourse") }
            CourseChip("Handgun Safety") { navController.navigate("handgunsafety") }
            CourseChip("Pistol Qualification") { navController.navigate("pistolqual") }
            CourseChip("Youth Core Program") { navController.navigate("courses") }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Facilities
        Text(
            text = "OUR FACILITIES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureIcon("🎯", "Pistol") { navController.navigate("pistolrange") }
            FeatureIcon("🔫", "Rifle") { navController.navigate("riflerange") }
            FeatureIcon("🏹", "Archery") { navController.navigate("archeryrange") }
            FeatureIcon("🥏", "Trap") { navController.navigate("traprange") }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contact Info
        InfoCard(title = "📍 CONTACT US") {
            Text("Kelowna and District Fish & Game Club", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("4041 Casorso Rd.", color = Color(0xFFCCCCCC), fontSize = 13.sp)
            Text("Kelowna, BC, V1W 4N6", color = Color(0xFFCCCCCC), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📧 info@kdfgc.org", color = Color(0xFF90EE90), fontSize = 13.sp)
            Text("📞 250.764.7558", color = Color(0xFF90EE90), fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TopNavItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun NavTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(if (selected) Modifier.background(Color(0xFF007236)) else Modifier)
            .clickable { onClick() }
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
fun CTAButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
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
fun EventItem(title: String, details: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(text = details, color = Color(0xFF888888), fontSize = 11.sp)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun CourseChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2A2A2A))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF90EE90),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FeatureIcon(emoji: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2A2A2A))
            .clickable { onClick() }
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