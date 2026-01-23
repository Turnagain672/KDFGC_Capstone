package com.example.capstone2.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DocumentsScreen(navController: NavController) {
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedDocType by remember { mutableStateOf("") }
    var uploadSuccess by remember { mutableStateOf(false) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadSuccess = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF007236))
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Documents", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Button(
                onClick = { showUploadDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("UPLOAD DOCUMENT", fontWeight = FontWeight.Bold)
            }

            if (uploadSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Document uploaded! Admin will verify shortly.",
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("CERTIFICATES (LIFETIME)", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("These never expire once obtained", color = Color(0xFF888888), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(12.dp))

            CertDocumentItem(
                title = "CFSC Certificate",
                subtitle = "Canadian Firearms Safety Course",
                isCompleted = true,
                issueDate = "March 15, 2023",
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = true,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "CRFSC Certificate",
                subtitle = "Canadian Restricted Firearms Safety Course",
                isCompleted = true,
                issueDate = "March 15, 2023",
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = true,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "New Member Orientation",
                subtitle = "Club safety orientation",
                isCompleted = true,
                issueDate = "January 10, 2023",
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = true,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "Range Safety Certificate",
                subtitle = "Club range safety training",
                isCompleted = true,
                issueDate = "April 2, 2023",
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = true,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("LICENSES (5 YEAR EXPIRY)", color = Color(0xFFFFEB3B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Must be renewed every 5 years with RCMP", color = Color(0xFF888888), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(12.dp))

            CertDocumentItem(
                title = "PAL License",
                subtitle = "Possession & Acquisition License",
                isCompleted = true,
                issueDate = "March 15, 2023",
                expiryDate = "March 15, 2028",
                daysUntilExpiry = 790,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "RPAL License",
                subtitle = "Restricted PAL",
                isCompleted = true,
                issueDate = "March 15, 2023",
                expiryDate = "March 15, 2028",
                daysUntilExpiry = 790,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("CLUB QUALIFICATIONS (ANNUAL)", color = Color(0xFFFF9800), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Must be renewed yearly at the club", color = Color(0xFF888888), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(12.dp))

            CertDocumentItem(
                title = "Handgun Safety Qualification",
                subtitle = "Required for handgun use",
                isCompleted = true,
                issueDate = "January 30, 2025",
                expiryDate = "January 30, 2026",
                daysUntilExpiry = 8,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "Pistol Qualification",
                subtitle = "Advanced pistol certification",
                isCompleted = false,
                issueDate = null,
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("WAIVERS & FORMS (ANNUAL)", color = Color(0xFFFF9800), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Must be signed yearly", color = Color(0xFF888888), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(12.dp))

            CertDocumentItem(
                title = "Liability Waiver",
                subtitle = "Club liability release",
                isCompleted = true,
                issueDate = "January 10, 2026",
                expiryDate = "January 10, 2027",
                daysUntilExpiry = 353,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "Range Rules Agreement",
                subtitle = "Acknowledgment of range rules",
                isCompleted = true,
                issueDate = "January 10, 2026",
                expiryDate = "January 10, 2027",
                daysUntilExpiry = 353,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            CertDocumentItem(
                title = "Emergency Contact Form",
                subtitle = "Emergency contact information",
                isCompleted = true,
                issueDate = "June 5, 2024",
                expiryDate = null,
                daysUntilExpiry = null,
                isLifetime = true,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("MEMBERSHIP", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            CertDocumentItem(
                title = "Membership Card",
                subtitle = "Digital membership card",
                isCompleted = true,
                issueDate = "January 5, 2026",
                expiryDate = "December 31, 2026",
                daysUntilExpiry = 343,
                isLifetime = false,
                onUpload = { filePicker.launch("*/*") },
                onDownload = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("MISSING DOCUMENTS", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            MissingDocItem("Pistol Qualification", "Required for pistol range access")

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Upload Document", color = Color.White) },
            text = {
                Column {
                    Text("Select document type:", color = Color(0xFFCCCCCC), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val docTypes = listOf(
                        "PAL License",
                        "RPAL License",
                        "CFSC Certificate",
                        "CRFSC Certificate",
                        "Handgun Safety Qualification",
                        "Pistol Qualification",
                        "Liability Waiver",
                        "Range Rules Agreement",
                        "Emergency Contact Form",
                        "Other"
                    )

                    docTypes.forEach { docType ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDocType = docType }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedDocType == docType,
                                onClick = { selectedDocType = docType },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF90EE90),
                                    unselectedColor = Color(0xFF888888)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(docType, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedDocType.isNotEmpty()) {
                            showUploadDialog = false
                            filePicker.launch("*/*")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    enabled = selectedDocType.isNotEmpty()
                ) {
                    Text("SELECT FILE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }
}

@Composable
fun CertDocumentItem(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    issueDate: String?,
    expiryDate: String?,
    daysUntilExpiry: Int?,
    isLifetime: Boolean,
    onUpload: () -> Unit,
    onDownload: () -> Unit
) {
    val statusColor = when {
        !isCompleted -> Color(0xFFFF5252)
        isLifetime -> Color(0xFF4CAF50)
        daysUntilExpiry != null && daysUntilExpiry <= 15 -> Color(0xFFFF5252)
        daysUntilExpiry != null && daysUntilExpiry <= 30 -> Color(0xFFFF9800)
        daysUntilExpiry != null && daysUntilExpiry <= 45 -> Color(0xFFFFEB3B)
        else -> Color(0xFF4CAF50)
    }

    val expiryText = when {
        !isCompleted -> "NOT COMPLETED"
        isLifetime -> "LIFETIME"
        daysUntilExpiry != null && daysUntilExpiry <= 15 -> "EXPIRES IN $daysUntilExpiry DAYS!"
        daysUntilExpiry != null && daysUntilExpiry <= 30 -> "$daysUntilExpiry days left"
        daysUntilExpiry != null && daysUntilExpiry <= 45 -> "$daysUntilExpiry days left"
        expiryDate != null -> "Expires: $expiryDate"
        else -> ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color(0xFF888888), fontSize = 11.sp)
                if (issueDate != null) {
                    Text("Issued: $issueDate", color = Color(0xFF666666), fontSize = 10.sp)
                }
                if (expiryText.isNotEmpty()) {
                    Text(expiryText, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (isCompleted) {
                IconButton(onClick = onDownload) {
                    Icon(Icons.Default.Download, contentDescription = "Download", tint = Color(0xFF90EE90), modifier = Modifier.size(20.dp))
                }
            }
            IconButton(onClick = onUpload) {
                Icon(Icons.Default.Upload, contentDescription = "Upload", tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun MissingDocItem(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3D1F1F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFFF5252),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(description, color = Color(0xFFFF8A80), fontSize = 11.sp)
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFFF5252),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}