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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.Course

@Composable
fun CoursesScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val allCourses by userViewModel.allCourses.collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var showRegisterDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF003D1F), Color(0xFF007236))))
                .padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(text = "Courses & Training", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (allCourses.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
                    Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📚", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Courses Available", color = Color.White, fontSize = 16.sp)
                        Text("Check back soon for upcoming courses", color = Color(0xFF888888), fontSize = 13.sp)
                    }
                }
            } else {
                Text("AVAILABLE COURSES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                allCourses.forEach { course ->
                    CourseListCard(
                        course = course,
                        onRegisterClick = { selectedCourse = course; showRegisterDialog = true },
                        onContactClick = { navController.navigate("contact") }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = Color(0xFF2196F3), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Course Information", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("• Registration is required for all courses", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Payment is due upon confirmation", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• Cancellations must be made 48 hours in advance", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Text("• All courses include required materials", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Questions? Contact us at info@kdfgc.org", color = Color(0xFF90EE90), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showRegisterDialog && selectedCourse != null) {
        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            containerColor = Color(0xFF252525),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, null, tint = Color(0xFF90EE90), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Register for Course", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(selectedCourse!!.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(selectedCourse!!.description, color = Color(0xFFCCCCCC), fontSize = 13.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFF444444))
                    Spacer(modifier = Modifier.height(12.dp))
                    CourseDetailRow("📅 Date", selectedCourse!!.date)
                    CourseDetailRow("🕐 Time", selectedCourse!!.time)
                    CourseDetailRow("📍 Location", selectedCourse!!.location)
                    CourseDetailRow("💰 Cost", selectedCourse!!.cost)
                    CourseDetailRow("👥 Seats Left", "${selectedCourse!!.seatsRemaining}/${selectedCourse!!.classSize}")
                    CourseDetailRow("👨‍🏫 Instructor", selectedCourse!!.instructorName)
                    if (currentUser == null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("⚠️ Please log in to register for this course", color = Color(0xFFFF9800), fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                if (currentUser != null && selectedCourse!!.seatsRemaining > 0) {
                    Button(onClick = {
                        userViewModel.registerForCourse(selectedCourse!!.id, currentUser!!.id, currentUser!!.fullName, selectedCourse!!.name)
                        showRegisterDialog = false
                        showSuccessDialog = true
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("REGISTER")
                    }
                } else if (currentUser == null) {
                    Button(onClick = { showRegisterDialog = false; navController.navigate("login") }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))) {
                        Text("LOG IN")
                    }
                } else {
                    Button(onClick = { }, enabled = false, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666666))) {
                        Text("FULL")
                    }
                }
            },
            dismissButton = { OutlinedButton(onClick = { showRegisterDialog = false }) { Text("CANCEL", color = Color(0xFF888888)) } }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = Color(0xFF252525),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registration Submitted!", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text("Your registration has been submitted. An admin will contact you shortly with payment details and confirmation.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("📧 Check your email for updates", color = Color(0xFF90EE90), fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236))) { Text("OK") }
            }
        )
    }
}

@Composable
fun CourseListCard(course: Course, onRegisterClick: () -> Unit, onContactClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(course.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("${course.date} • ${course.time}", color = Color(0xFF888888), fontSize = 12.sp)
                }
                Text(course.cost, color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(course.description, color = Color(0xFFCCCCCC), fontSize = 13.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("📍 ${course.location}", color = Color(0xFF888888), fontSize = 11.sp)
                    Text("👨‍🏫 ${course.instructorName}", color = Color(0xFF888888), fontSize = 11.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${course.seatsRemaining}/${course.classSize}", color = if (course.seatsRemaining <= 3) Color(0xFFFF6B6B) else Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("seats", color = Color(0xFF888888), fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRegisterClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), enabled = course.seatsRemaining > 0, shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(vertical = 10.dp)) {
                    Icon(Icons.Default.HowToReg, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (course.seatsRemaining > 0) "REGISTER" else "FULL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = onContactClick, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(vertical = 10.dp)) {
                    Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp), tint = Color(0xFF90EE90))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CONTACT", fontSize = 12.sp, color = Color(0xFF90EE90), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CourseDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF888888), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}