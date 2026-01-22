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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(navController: NavController, articleId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        TopAppBar(
            title = { Text("News", color = Color.White) },
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
            when (articleId) {
                "pistol" -> {
                    Text(
                        "KDFGC Pistol Members, Recreational Pistol",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Improve Your Pistol Skills",
                        fontSize = 16.sp,
                        color = Color(0xFF90EE90)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Are you looking to improve your pistol shooting skills? KDFGC offers several programs for recreational pistol shooters.\n\n" +
                                        "Our pistol range is open to all members with valid RPAL and completed Handgun Safety Qualification.\n\n" +
                                        "Programs available:\n" +
                                        "• Recreational Pistol (Sessions run weekly)\n" +
                                        "• IPSC (IPSC BC) practical shooting\n" +
                                        "• Precision Pistol BCTSA\n" +
                                        "• Handgun 1500 (PPC)\n" +
                                        "• Women Pistol Discipline\n\n" +
                                        "Requirements:\n" +
                                        "• Valid RPAL (Restricted Possession and Acquisition License)\n" +
                                        "• Completed Handgun Safety Qualification Course\n" +
                                        "• Active KDFGC membership\n\n" +
                                        "Contact the club office for more information.\n" +
                                        "Phone: 250.764.7558\n" +
                                        "Email: info@kdfgc.org",
                                color = Color(0xFFCCCCCC),
                                fontSize = 15.sp,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
                "youth" -> {
                    Text(
                        "Youth Core Program This Winter",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Programs available for youth members",
                        fontSize = 16.sp,
                        color = Color(0xFF90EE90)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "KDFGC is proud to offer youth programs this winter season!\n\n" +
                                        "Our Youth Core Program introduces young people to safe and responsible shooting sports.\n\n" +
                                        "Programs include:\n" +
                                        "• Junior Archery (BCAA JOP) - Junior Olympic Program\n" +
                                        "• Youth Rifle Safety\n" +
                                        "• Introduction to Trap Shooting\n" +
                                        "• Wildlife Conservation Education\n\n" +
                                        "All programs are supervised by certified instructors. Equipment is provided.\n\n" +
                                        "Ages: 10-17 years\n" +
                                        "Parent/guardian consent required.\n\n" +
                                        "Upcoming Sessions:\n" +
                                        "• January 31, 2026\n" +
                                        "• February 7, 2026\n\n" +
                                        "Register online or contact the club office.\n" +
                                        "Phone: 250.764.7558\n" +
                                        "Email: info@kdfgc.org",
                                color = Color(0xFFCCCCCC),
                                fontSize = 15.sp,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        "Article Not Found",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "The requested article could not be found.",
                        color = Color(0xFFCCCCCC),
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Back button at bottom too
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF90EE90)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("BACK TO HOME", fontWeight = FontWeight.Bold)
            }
        }
    }
}