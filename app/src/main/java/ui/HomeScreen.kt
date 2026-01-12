package com.example.kdfgc_capstone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kdfgc_capstone.R

// Sample data classes for announcements and events
data class Announcement(val title: String, val date: String, val content: String)
data class Event(val title: String, val date: String, val location: String)

@Composable
fun HomeScreen(
    onEventsClick: () -> Unit,
    onMembershipClick: () -> Unit,
    onContactClick: () -> Unit
) {
    val announcements = remember {
        listOf(
            Announcement("New Safety Protocols", "Jan 10, 2026", "Please review updated safety protocols."),
            Announcement("Annual General Meeting", "Feb 5, 2026", "Join us for the AGM at 7 PM."),
            Announcement("Range Maintenance", "Jan 20, 2026", "Range will be closed for maintenance.")
        )
    }
    val upcomingEvents = remember {
        listOf(
            Event("Winter Shooting Competition", "Jan 25, 2026", "Main Range"),
            Event("Youth Archery Camp", "Feb 15, 2026", "Archery Range"),
            Event("Family Fishing Day", "Mar 10, 2026", "Lakefront")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2E7D32), Color(0xFFE8F5E9))
                )
            )
            .padding(16.dp)
    ) {
        item {
            // Hero image
            Image(
                painter = painterResource(id = R.drawable.generated_image_2026_01_12),
                contentDescription = "Kelowna Fish & Game Club",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 16.dp)
                    .background(
                        color = Color(0xFF1B5E20),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }

        item {
            Text(
                text = "Welcome to Kelowna Fish & Game Club",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Connecting outdoor enthusiasts, families, and conservationists for over 50 years.",
                fontSize = 16.sp,
                color = Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 24.dp),
                lineHeight = 22.sp
            )
        }

        item {
            SectionTitle("Announcements")
            announcements.forEach { announcement ->
                AnnouncementCard(announcement)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Upcoming Events")
            upcomingEvents.forEach { event ->
                EventCard(event)
            }
            Button(
                onClick = onEventsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Filled.Event, contentDescription = "View All Events", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("View All Events", fontSize = 18.sp)
            }
        }

        item {
            SectionTitle("Membership")
            Text(
                "Join or renew your membership to support the club and enjoy member benefits.",
                fontSize = 16.sp,
                color = Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Button(
                onClick = onMembershipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Filled.Group, contentDescription = "Membership", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Membership", fontSize = 18.sp)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Range Status")
            Text(
                "Main Range: Open\nArchery Range: Closed for Maintenance\nHours: 8:00 AM - 8:00 PM",
                fontSize = 16.sp,
                color = Color(0xFF388E3C)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Contact & Quick Links")
            ContactInfoCard(onContactClick)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Footer()
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1B5E20),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(announcement.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
            Text(announcement.date, fontSize = 12.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            Text(announcement.content, fontSize = 14.sp, color = Color(0xFF1B5E20), maxLines = 3, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.CalendarToday, contentDescription = "Event Date", tint = Color(0xFF2E7D32))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B5E20))
                Text("${event.date} • ${event.location}", fontSize = 14.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun ContactInfoCard(onContactClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onContactClick() }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact Us", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
            Text("Email: info@kdfgc.org", fontSize = 14.sp, color = Color(0xFF1B5E20))
            Text("Phone: (250) 555-1234", fontSize = 14.sp, color = Color(0xFF1B5E20))
            Text("Address: 123 Fish & Game Rd, Kelowna, BC", fontSize = 14.sp, color = Color(0xFF1B5E20))
        }
    }
}

@Composable
fun Footer() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("© 2026 Kelowna Fish & Game Club", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Privacy Policy", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
            Text("Terms of Service", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
            Text("Facebook", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}