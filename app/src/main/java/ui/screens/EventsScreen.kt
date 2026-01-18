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
                    text = "January 2026 • Kelowna & District Fish and Game Club",
                    fontSize = 12.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // This Week
        SectionHeader("THIS WEEK")

        EventCard(
            title = "Sunday Night Archery League",
            date = "Sunday, January 19, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Indoor Archery Range",
            description = "Session 3 of 13. Weekly league competition for all skill levels.",
            category = "Archery"
        )

        EventCard(
            title = "Open Archery",
            date = "Monday, January 20, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Archery Range",
            description = "Session 9 of 15. Open practice for members.",
            category = "Archery"
        )

        EventCard(
            title = "Precision Pistol BCTSA",
            date = "Tuesday, January 21, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Indoor Pistol Range",
            description = "BC Target Sports Association precision pistol competition.",
            category = "Pistol"
        )

        EventCard(
            title = "Handgun 1500 (PPC)",
            date = "Wednesday, January 22, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Pistol Range",
            description = "Police Pistol Combat style competition. All skill levels.",
            category = "Pistol"
        )

        EventCard(
            title = "IPSC (IPSC BC)",
            date = "Thursday, January 23, 2026",
            time = "6:00 PM - 9:00 PM",
            location = "Action Pistol Bay",
            description = "International Practical Shooting Confederation match.",
            category = "Pistol"
        )

        EventCard(
            title = "Trapshooting",
            date = "Saturday, January 25, 2026",
            time = "10:00 AM - 2:00 PM",
            location = "Trap Range",
            description = "Weekly trap shooting. Shells available for purchase.",
            category = "Shotgun"
        )

        EventCard(
            title = "New Member Orientation",
            date = "Saturday, January 25, 2026",
            time = "10:00 AM - 11:30 AM",
            location = "Clubhouse",
            description = "Mandatory orientation for new members. Registration required. This training is a club and legal requirement prior to shooting unsupervised.",
            category = "Training"
        )

        // Courses Section
        SectionHeader("COURSES")

        EventCard(
            title = "Canadian Firearms Safety Course (PAL)",
            date = "Saturday, January 25, 2026",
            time = "9:00 AM - 5:00 PM",
            location = "Clubhouse",
            description = "Non-Restricted PAL course. Certification for firearm possession and acquisition.",
            category = "Course"
        )

        EventCard(
            title = "Handgun Safety Qualification Course",
            date = "Saturday, February 1, 2026",
            time = "9:00 AM - 4:00 PM",
            location = "Indoor Range",
            description = "Required qualification for handgun use at the club.",
            category = "Course"
        )

        // Coming Up Section
        SectionHeader("COMING UP")

        EventCard(
            title = "Women Pistol Discipline",
            date = "Saturday, February 1, 2026",
            time = "10:00 AM - 12:00 PM",
            location = "Pistol Range",
            description = "Women's pistol training and practice session.",
            category = "Pistol"
        )

        EventCard(
            title = "Youth Core Program",
            date = "Saturday, February 1, 2026",
            time = "9:00 AM - 12:00 PM",
            location = "Various Ranges",
            description = "Youth shooting sports program. Ages 12-17.",
            category = "Youth"
        )

        EventCard(
            title = "Precision Rifle",
            date = "Saturday, February 1, 2026",
            time = "9:00 AM - 4:00 PM",
            location = "Outdoor Rifle Range",
            description = "Long range precision rifle practice and competition.",
            category = "Rifle"
        )

        EventCard(
            title = "Target Rimfire",
            date = "Saturday, February 1, 2026",
            time = "10:00 AM - 12:00 PM",
            location = "Rifle Range",
            description = "Rimfire rifle target shooting for all skill levels.",
            category = "Rifle"
        )

        EventCard(
            title = "Junior Archery (BCAA JOP)",
            date = "Saturday, February 1, 2026",
            time = "9:00 AM - 10:30 AM",
            location = "Archery Range",
            description = "Session 15 of 25. BC Archery Association Junior Olympic Program.",
            category = "Youth"
        )

        EventCard(
            title = "Adult Archery (BCAA)",
            date = "Saturday, February 1, 2026",
            time = "10:30 AM - 12:00 PM",
            location = "Archery Range",
            description = "Session 15 of 25. BC Archery Association adult program.",
            category = "Archery"
        )

        EventCard(
            title = "Recreational Pistol",
            date = "Saturday, February 8, 2026",
            time = "1:00 PM - 4:00 PM",
            location = "Pistol Range",
            description = "Improve your pistol skills. Open to all members.",
            category = "Pistol"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF90EE90),
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun EventCard(
    title: String,
    date: String,
    time: String,
    location: String,
    description: String,
    category: String
) {
    val categoryColor = when (category) {
        "Archery" -> Color(0xFF4CAF50)
        "Pistol" -> Color(0xFF2196F3)
        "Rifle" -> Color(0xFFFF9800)
        "Shotgun" -> Color(0xFF9C27B0)
        "Course" -> Color(0xFFE91E63)
        "Training" -> Color(0xFFFFEB3B)
        "Youth" -> Color(0xFF00BCD4)
        else -> Color(0xFF007236)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(categoryColor)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = category,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
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