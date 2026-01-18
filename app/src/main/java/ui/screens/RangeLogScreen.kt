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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstone2.data.RangeLog
import com.example.capstone2.data.RangeLogViewModel

@Composable
fun RangeLogScreen(viewModel: RangeLogViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    var showAddDialog by remember { mutableStateOf(false) }

    val logs by viewModel.allLogs.collectAsState(initial = emptyList())
    val totalVisits by viewModel.totalVisits.collectAsState(initial = 0)
    val totalRounds by viewModel.totalRounds.collectAsState(initial = 0)

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
            StatCard("Total Visits", "$totalVisits", "🏆")
            StatCard("This Month", "${logs.size}", "📅")
            StatCard("Rounds Fired", "$totalRounds", "💥")
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

            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No sessions logged yet.\nTap + to add your first session!",
                        color = Color(0xFF888888),
                        fontSize = 14.sp
                    )
                }
            } else {
                logs.forEach { log ->
                    LogEntry(
                        log = log,
                        onDelete = { viewModel.deleteLog(log) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Log Dialog
    if (showAddDialog) {
        AddLogDialog(
            onDismiss = { showAddDialog = false },
            onSave = { date, range, firearm, rounds, notes ->
                viewModel.addLog(date, range, firearm, rounds, notes)
                showAddDialog = false
            }
        )
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
    log: RangeLog,
    onDelete: () -> Unit
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
                    text = log.date,
                    color = Color(0xFF90EE90),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF007236))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${log.rounds} rds",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF888888),
                            modifier = Modifier.size(18.dp)
                        )
                    }
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
                Text(text = log.range, color = Color.White, fontSize = 13.sp)
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
                Text(text = log.firearm, color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = log.notes,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AddLogDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int, String) -> Unit
) {
    var date by remember { mutableStateOf("") }
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
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (e.g., Jan 18, 2026)", color = Color(0xFF888888)) },
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
                onClick = {
                    val roundsInt = rounds.toIntOrNull() ?: 0
                    if (date.isNotBlank() && range.isNotBlank()) {
                        onSave(date, range, firearm, roundsInt, notes)
                    }
                },
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