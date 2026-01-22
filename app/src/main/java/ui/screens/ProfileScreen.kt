package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val currentUser by userViewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Header with Profile
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
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser?.fullName?.take(2)?.uppercase() ?: "JD",
                        color = Color(0xFF007236),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentUser?.fullName ?: "John Doe",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Member since 2023",
                    fontSize = 13.sp,
                    color = Color(0xFFCCCCCC)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF90EE90))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "✓ ACTIVE MEMBER",
                        color = Color(0xFF003D1F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // My Account Button (Passes & Cards)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    if (currentUser != null) {
                        navController.navigate("myaccount")
                    } else {
                        navController.navigate("login")
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF007236))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "My Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (currentUser != null) "View passes, cards & purchases" else "Log in to view your passes",
                        color = Color(0xFFCCFFCC),
                        fontSize = 12.sp
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Membership Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MEMBERSHIP DETAILS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF90EE90),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                MembershipRow("Member ID", currentUser?.memberNumber ?: "KDFGC-2023-0847")
                MembershipRow("Membership Type", "Full Adult")
                MembershipRow("Expiry Date", "December 31, 2026")
                MembershipRow("Range Certified", "Indoor, Outdoor, Archery")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileStatCard("Range Visits", "24", Icons.Default.LocationOn)
            ProfileStatCard("Events", "8", Icons.Default.Event)
            ProfileStatCard("Courses", "3", Icons.Default.School)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Certifications Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CERTIFICATIONS & QUALIFICATIONS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF90EE90),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                CertRow("PAL (Non-Restricted)", true)
                CertRow("RPAL (Restricted)", true)
                CertRow("New Member Orientation", true)
                CertRow("Handgun Safety Qualification", true)
                CertRow("Pistol Safety Qualification", false)
                CertRow("Handgun Basic Course", false)
                CertRow("Handgun Level 2 Course", false)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section
        Text(
            text = "SETTINGS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(Icons.Default.Person, "Edit Profile", "Update your information") { navController.navigate("editprofile") }
        SettingsItem(Icons.Default.Notifications, "Notifications", "Manage alerts") { navController.navigate("notifications") }
        SettingsItem(Icons.Default.Security, "Privacy & Security", "Account settings") { navController.navigate("privacy") }
        SettingsItem(Icons.Default.CreditCard, "Renew Membership", "Expires Dec 2026") { navController.navigate("renew") }
        SettingsItem(Icons.Default.Description, "Documents", "Certificates & waivers") { navController.navigate("documents") }
        SettingsItem(Icons.Default.Help, "Help & Support", "FAQs and contact") { navController.navigate("help") }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOG OUT", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MembershipRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFF888888), fontSize = 13.sp)
        Text(text = value, color = Color(0xFF90EE90), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun CertRow(name: String, completed: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (completed) Color(0xFF4CAF50) else Color(0xFF555555),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = name, color = if (completed) Color.White else Color(0xFF888888), fontSize = 13.sp)
        }
        Text(
            text = if (completed) "✓" else "—",
            color = if (completed) Color(0xFF4CAF50) else Color(0xFF555555),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.width(105.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF007236), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color(0xFF90EE90), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = Color(0xFF888888), fontSize = 10.sp)
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF007236)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = Color(0xFF888888), fontSize = 11.sp)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
        }
    }
}