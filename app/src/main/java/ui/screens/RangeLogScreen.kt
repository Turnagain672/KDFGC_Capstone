package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RangeLogScreen() {
    val scrollState = rememberScrollState()
    var showAddDialog by remember { mutableStateOf(false) }

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
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF007236))
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🎯 RANGE LOG",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Track your shooting sessions",
                        fontSize = 12.sp,
                        color = Color(0xFFCCCCCC)
                    )
                }
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color.White,
                    contentColor = Color(0xFF007236),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Log")
                }
            }
        }

        // Stats Summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard("Total Visits", "24", "🏆")
            StatCard("This Month", "6", "📅")
            StatCard("Rounds Fired", "1,850", "💥")
        }

        // Log Entries
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "RECENT SESSIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF90EE90),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LogEntry(
                date = "Jan 15, 2026",
                range = "Indoor Range",
                firearm = "Glock 19",
                rounds = 150,
                notes = "Working on trigger control. Groupings improved at 15 yards."
            )

            LogEntry(
                date = "Jan 12, 2026",
                range = "Outdoor Rifle",
                firearm = "Ruger 10/22",
                rounds = 200,
                notes = "Zeroed new scope at 50 yards. Great conditions."
            )

            LogEntry(
                date = "Jan 8, 2026",
                range = "Indoor Range",
                firearm = "Smith & Wesson M&P",
                rounds = 100,
                notes = "Practice session with new holster draw."
            )

            LogEntry(
                date = "Jan 3, 2026",
                range = "Archery Range",
                firearm = "Compound Bow",
                rounds = 60,
                notes = "3D target practice. Hit 8/10 vitals."
            )

            LogEntry(
                date = "Dec 28, 2025",
                range = "Outdoor Rifle",
                firearm = "AR-15",
                rounds = 120,
                notes = "200 yard practice. Need to adjust windage."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Log Dialog
    if (showAddDialog) {
        AddLogDialog(onDismiss = { showAddDialog = false })
    }
}

@Composable
fun StatCard(label: String, value: String, emoji: String) {
    Card(
        modifier = Modifier.width(105.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = Color(0xFF90EE90),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color(0xFF888888),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LogEntry(
    date: String,
    range: String,
    firearm: String,
    rounds: Int,
    notes: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date,
                    color = Color(0xFF90EE90),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF007236))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$rounds rds",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = range, color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Gavel,
                    contentDescription = null,
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = firearm, color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = notes,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AddLogDialog(onDismiss: () -> Unit) {
    var range by remember { mutableStateOf("") }
    var firearm by remember { mutableStateOf("") }
    var rounds by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF252525),
        title = {
            Text("Add Range Session", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = range,
                    onValueChange = { range = it },
                    label = { Text("Range", color = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = firearm,
                    onValueChange = { firearm = it },
                    label = { Text("Firearm", color = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = rounds,
                    onValueChange = { rounds = it },
                    label = { Text("Rounds Fired", color = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes", color = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF888888))
            }
        }
    )
}