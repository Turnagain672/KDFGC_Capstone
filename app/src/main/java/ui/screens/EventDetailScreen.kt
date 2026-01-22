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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavController, eventName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        TopAppBar(
            title = { Text("Event Details", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF003D1F))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF007236), Color(0xFF009944))),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(20.dp)
            ) {
                Text(eventName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📅 Event Information", color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    when {
                        eventName.contains("IPSC", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Pistol Range")
                            InfoRow("🕐 Schedule", "Session 10 of 19")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("IPSC (International Practical Shooting Confederation) matches hosted by IPSC BC. Competitive practical shooting for licensed handgun owners.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Open Archery", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Archery Range")
                            InfoRow("🕐 Schedule", "Session 16 of 26")
                            InfoRow("💰 Cost", "Members: Free")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Open archery sessions for all skill levels. Equipment available. Great for practice and casual shooting.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Recreational Pistol", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Pistol Range")
                            InfoRow("🕐 Schedule", "Session 10 of 19")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Recreational pistol shooting for RPAL holders. Improve your skills in a relaxed environment. Handgun Safety Qualification required.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("PAL", ignoreCase = true) || eventName.contains("Firearms Safety", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Clubhouse")
                            InfoRow("🕐 Duration", "Full Day Course")
                            InfoRow("💰 Cost", "$150")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Canadian Firearms Safety Course (PAL - Non Restricted). Required to obtain your Possession and Acquisition License for rifles and shotguns. Includes manual, exam, and certificate.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Trap", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Trap Range")
                            InfoRow("🕐 Schedule", "Session 66 of 84")
                            InfoRow("💰 Cost", "$5 per round")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Weekly trapshooting sessions. All skill levels welcome. Bring your own shotgun or rent one at the range. Great for beginners and experienced shooters alike.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Junior Archery", ignoreCase = true) || eventName.contains("JOP", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Archery Range")
                            InfoRow("🕐 Program", "BCAA JOP")
                            InfoRow("💰 Cost", "See registration")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Junior Olympic Program archery for youth. Certified BCAA instruction. Equipment provided. Ages 10-17.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Adult Archery", ignoreCase = true) || eventName.contains("BCAA", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Archery Range")
                            InfoRow("🕐 Program", "BCAA")
                            InfoRow("💰 Cost", "See registration")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Adult archery program with BC Archery Association certified instruction. All skill levels welcome.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Sunday Night Archery", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Indoor Archery Range")
                            InfoRow("🕐 Time", "Sunday Evenings")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Sunday Night Archery League. Competitive league shooting for members. Register for the full season.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Precision Rifle", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Rifle Range")
                            InfoRow("🕐 Schedule", "See calendar")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Precision rifle shooting for experienced shooters. Long range accuracy and marksmanship training.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Precision Pistol", ignoreCase = true) || eventName.contains("BCTSA", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Pistol Range")
                            InfoRow("🕐 Program", "BCTSA")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Precision Pistol shooting with BC Target Sports Association. Competitive bullseye shooting for RPAL holders.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Handgun 1500", ignoreCase = true) || eventName.contains("PPC", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Pistol Range")
                            InfoRow("🕐 Schedule", "Weekly")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Handgun 1500 (PPC - Police Pistol Combat) course. Timed shooting at various distances. RPAL and Handgun Safety Qualification required.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Women Pistol", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Pistol Range")
                            InfoRow("🕐 Schedule", "See calendar")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Women's Pistol Discipline. A supportive environment for women to learn and practice pistol shooting. RPAL required.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Target Rimfire", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Rifle Range")
                            InfoRow("🕐 Schedule", "See calendar")
                            InfoRow("💰 Cost", "Members Only")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Target rimfire shooting. Practice your accuracy with .22 caliber rifles. Great for beginners and experienced shooters.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("New Member Orientation", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Clubhouse")
                            InfoRow("🕐 Time", "10:00 - 11:30 AM")
                            InfoRow("💰 Cost", "Free for new members")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Required orientation for all new members. Learn range rules, safety procedures, and club policies. Must complete before shooting unsupervised at the range.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Youth Core Program", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Various Ranges")
                            InfoRow("🕐 Schedule", "See registration")
                            InfoRow("💰 Cost", "See registration")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Youth Core Program introducing young people to shooting sports. Includes archery, rifle safety, and conservation education. Ages 10-17. Parent/guardian consent required.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        eventName.contains("Archery Loonie", ignoreCase = true) -> {
                            InfoRow("📍 Location", "Archery Range")
                            InfoRow("🕐 Time", "Sundays")
                            InfoRow("💰 Cost", "$1 per round")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Archery Loonie Sunday! Casual archery shooting for just $1 per round. Great for families and beginners.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                        else -> {
                            InfoRow("📍 Location", "KDFGC Range")
                            InfoRow("🕐 Time", "See schedule")
                            InfoRow("💰 Cost", "Varies")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Contact the club office for more information about this event.\n\nPhone: 250.764.7558\nEmail: info@kdfgc.org", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("register/${eventName}") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("REGISTER FOR EVENT", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF888888), fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}