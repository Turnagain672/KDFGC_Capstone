package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MyAccountScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val purchases by currentUser?.let {
        userViewModel.getPurchasesForUser(it.id).collectAsState(initial = emptyList())
    } ?: remember { mutableStateOf(emptyList()) }

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate("login") {
                popUpTo("myaccount") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
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
                onClick = { navController.navigate("home") },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "My Account",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = {
                    userViewModel.logout()
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF007236), RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = currentUser?.fullName ?: "Member",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentUser?.email ?: "",
                            color = Color(0xFF888888),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Member #${currentUser?.memberNumber ?: "N/A"}",
                            color = Color(0xFF90EE90),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Purchases Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color(0xFF90EE90), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("MY PASSES & CARDS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (purchases.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎫", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Active Passes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text("Purchase passes from the store", color = Color(0xFF888888), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("store") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("GO TO STORE")
                        }
                    }
                }
            } else {
                purchases.forEach { purchase ->
                    PurchaseCard(purchase = purchase)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Text("QUICK ACTIONS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    text = "Store",
                    emoji = "🛒",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("store") }
                )
                QuickActionButton(
                    text = "Events",
                    emoji = "📅",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("events") }
                )
                QuickActionButton(
                    text = "Range Log",
                    emoji = "📝",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("rangelog") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PurchaseCard(purchase: com.example.capstone2.data.Purchase) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = purchase.itemName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(
                            if (purchase.remainingQuantity > 3) Color(0xFF007236) else Color(0xFFFF9800),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${purchase.remainingQuantity} left",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { purchase.remainingQuantity.toFloat() / purchase.totalQuantity.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF90EE90),
                trackColor = Color(0xFF444444)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Used: ${purchase.totalQuantity - purchase.remainingQuantity} of ${purchase.totalQuantity}",
                    color = Color(0xFF888888),
                    fontSize = 11.sp
                )
                Text(
                    text = "$${purchase.price}",
                    color = Color(0xFF888888),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(text: String, emoji: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = Color.White, fontSize = 12.sp)
        }
    }
}