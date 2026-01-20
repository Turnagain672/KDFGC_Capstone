package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.capstone2.data.Purchase
import com.example.capstone2.data.User

@Composable
fun AdminPanelScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMember by remember { mutableStateOf<User?>(null) }
    val currentUser by userViewModel.currentUser.collectAsState()
    val searchResults by userViewModel.searchResults.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser == null || currentUser?.isAdmin != true) {
            navController.navigate("login") {
                popUpTo("adminpanel") { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        userViewModel.getAllMembers()
    }

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
                        colors = listOf(Color(0xFF8B0000), Color(0xFFB22222))
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
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("ADMIN PANEL", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Member Management", color = Color(0xFFFFCCCC), fontSize = 12.sp)
            }
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

        if (selectedMember != null) {
            MemberDetailView(
                member = selectedMember!!,
                userViewModel = userViewModel,
                onBack = { selectedMember = null }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.isNotBlank()) {
                            userViewModel.searchMembers(it)
                        } else {
                            userViewModel.getAllMembers()
                        }
                    },
                    label = { Text("Search member by name") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB22222),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedLabelColor = Color(0xFFFF6B6B),
                        unfocusedLabelColor = Color(0xFF888888),
                        cursorColor = Color(0xFFFF6B6B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "MEMBERS (${searchResults.size})",
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (searchResults.isEmpty()) {
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
                            Text("👥", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No Members Found", color = Color.White, fontSize = 16.sp)
                            Text("Members will appear here after registration", color = Color(0xFF888888), fontSize = 13.sp)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        searchResults.forEach { member ->
                            MemberCard(
                                member = member,
                                onClick = { selectedMember = member }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemberCard(member: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
                    .size(44.dp)
                    .background(Color(0xFF007236), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.fullName.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.fullName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Member #${member.memberNumber}", color = Color(0xFF888888), fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF888888))
        }
    }
}

@Composable
fun MemberDetailView(
    member: User,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val purchases by userViewModel.getPurchasesForUser(member.id).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back button
        TextButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFFFF6B6B))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Back to Members", color = Color(0xFFFF6B6B))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Member Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF007236), RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.fullName.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(member.fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(member.email, color = Color(0xFF888888), fontSize = 13.sp)
                        Text("Member #${member.memberNumber}", color = Color(0xFF90EE90), fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("ACTIVE PASSES & CARDS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎫", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No Active Passes", color = Color.White, fontSize = 14.sp)
                    Text("This member has no active passes", color = Color(0xFF888888), fontSize = 12.sp)
                }
            }
        } else {
            purchases.forEach { purchase ->
                AdminPurchaseCard(
                    purchase = purchase,
                    onDeduct = { userViewModel.deductPurchase(purchase.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AdminPurchaseCard(purchase: Purchase, onDeduct: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Used: ${purchase.totalQuantity - purchase.remainingQuantity} of ${purchase.totalQuantity}",
                    color = Color(0xFF888888),
                    fontSize = 11.sp
                )

                Button(
                    onClick = { showConfirm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    enabled = purchase.remainingQuantity > 0
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DEDUCT 1", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Confirm Deduction", color = Color.White) },
            text = {
                Text(
                    "Deduct 1 use from '${purchase.itemName}'?\n\nRemaining after: ${purchase.remainingQuantity - 1}",
                    color = Color(0xFFCCCCCC)
                )
            },
            confirmButton = {
                TextButton(onClick = { onDeduct(); showConfirm = false }) {
                    Text("CONFIRM", color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }
}