package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun JoinScreen(navController: NavController) {
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
                text = "Become a Member",
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
            Text(
                text = "Join the Kelowna & District Fish and Game Club and get access to our world-class shooting facilities.",
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("MEMBERSHIP BENEFITS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            BenefitItem("Access to all shooting ranges")
            BenefitItem("Indoor and outdoor facilities")
            BenefitItem("Archery and trap ranges")
            BenefitItem("Member events and competitions")
            BenefitItem("Training courses and certifications")
            BenefitItem("Youth programs available")

            Spacer(modifier = Modifier.height(24.dp))

            Text("MEMBERSHIP OPTIONS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            MembershipOptionClickable("Adult Membership", "150.00", "Full access, ages 19+", navController)
            MembershipOptionClickable("Family Membership", "250.00", "2 adults + children under 19", navController)
            MembershipOptionClickable("Senior Membership", "100.00", "Ages 65+", navController)
            MembershipOptionClickable("Youth Membership", "75.00", "Ages 12-18, supervised", navController)

            Spacer(modifier = Modifier.height(24.dp))

            Text("REQUIREMENTS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("• Valid PAL or RPAL (for firearms)", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Complete New Member Orientation", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Sign liability waiver", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Agree to range rules", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("memberregistration") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("APPLY FOR MEMBERSHIP", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("BACK TO HOME", color = Color(0xFF888888), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BenefitItem(text: String) {
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
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun MembershipOptionClickable(title: String, price: String, description: String, navController: NavController) {
    val encodedTitle = URLEncoder.encode(title, "UTF-8")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate("checkout/$encodedTitle/$price") },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(description, color = Color(0xFF888888), fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$$price", color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Tap to purchase →", color = Color(0xFF007236), fontSize = 10.sp)
            }
        }
    }
}