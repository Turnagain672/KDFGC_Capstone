package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstone2.data.Course
import com.example.capstone2.data.KdfgcViewModel
import java.net.URLEncoder

@Composable
fun HomeScreen(navController: NavController, kdfgcViewModel: KdfgcViewModel = viewModel(), userViewModel: UserViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val kdfgcData by kdfgcViewModel.kdfgcData.collectAsState()
    val isLoading by kdfgcViewModel.isLoading.collectAsState()
    val error by kdfgcViewModel.error.collectAsState()
    val upcomingCourses by userViewModel.upcomingCourses.collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var showRegisterDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF003D1F))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("LOG IN", color = Color(0xFF007236), fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF005A2B), Color(0xFF007236))
                    )
                )
                .padding(vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = kdfgcData?.clubInfo?.name ?: "Kelowna & District\nFish and Game Club",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ESTABLISHED 1904 • WILDLIFE CONSERVATION",
                    fontSize = 9.sp,
                    color = Color(0xFFCCCCCC),
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2A2A))
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            NavTab("HOME", true) { }
            NavTab("EVENTS", false) { navController.navigate("events") }
            NavTab("RANGES", false) { navController.navigate("ranges") }
            NavTab("COURSES", false) { navController.navigate("courses") }
            NavTab("STORE", false) { navController.navigate("store") }
            NavTab("ABOUT", false) { navController.navigate("about") }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D25), Color(0xFF003318), Color(0xFF1A1A1A))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("🦌", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CTAButton("BECOME A MEMBER") { navController.navigate("join") }
                    CTAButton("RENEW") { navController.navigate("renew") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF90EE90))
            }
        }

        error?.let {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF3D2525))) {
                Text(text = "⚠️ Could not load latest data: $it", color = Color(0xFFFF6B6B), fontSize = 12.sp, modifier = Modifier.padding(12.dp))
            }
        }

        InfoCard(title = "🕐 RANGE HOURS") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Range Hours", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(kdfgcData?.clubInfo?.rangeHours ?: "8:00 AM - Dusk (Daily)", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                }
                Column {
                    Text("Office Hours", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(kdfgcData?.clubInfo?.officeHours ?: "Tue & Thu 6-8 PM, Sat 10 AM-2 PM", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                }
            }
        }

        InfoCard(title = "📢 RANGE UPDATES") {
            Text(
                text = "New Member Orientations are every second Saturday from 10:00 to 11:30. Please register online. This training is a club and legal requirement for new members prior to shooting unsupervised at the range.",
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        InfoCard(title = "📅 UPCOMING EVENTS") {
            val events = kdfgcData?.events
            if (events.isNullOrEmpty()) {
                ClickableEventItem("Sunday Night Archery League", "Session 3 of 13 • Indoor Range") { navController.navigate("eventdetail/Sunday Night Archery League") }
                ClickableEventItem("Open Archery", "Session 9 of 15 • Archery Range") { navController.navigate("eventdetail/Open Archery") }
                ClickableEventItem("Trapshooting", "Saturdays • Trap Range") { navController.navigate("eventdetail/Trapshooting") }
            } else {
                events.forEach { event ->
                    val encodedTitle = URLEncoder.encode(event.title, "UTF-8")
                    ClickableEventItem(event.title, "${event.date} • ${event.location}") { navController.navigate("eventdetail/$encodedTitle") }
                }
            }
        }

        InfoCard(title = "📰 FEATURED NEWS") {
            Column(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("article/pistol") }) {
                Text(text = "KDFGC Pistol Members, Recreational Pistol", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Improve Your Pistol Skills →", color = Color(0xFF90EE90), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(12.dp))
            Column(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("article/youth") }) {
                Text(text = "Youth Core Program This Winter", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Programs available for youth members →", color = Color(0xFF888888), fontSize = 12.sp)
            }
        }

        Text(
            text = "COURSES & TRAINING",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        if (upcomingCourses.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                upcomingCourses.forEach { course ->
                    CourseCard(course = course, onRegisterClick = { selectedCourse = course; showRegisterDialog = true }, onContactClick = { navController.navigate("contact") })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val courses = kdfgcData?.courses
                if (courses.isNullOrEmpty()) {
                    CourseChip("PAL Course") { navController.navigate("palcourse") }
                    CourseChip("RPAL Course") { navController.navigate("rpalcourse") }
                    CourseChip("Handgun Safety") { navController.navigate("handgunsafety") }
                    CourseChip("Pistol Qualification") { navController.navigate("pistolqual") }
                } else {
                    courses.forEach { course ->
                        CourseChip(course.name) {
                            when (course.id) {
                                "pal" -> navController.navigate("palcourse")
                                "rpal" -> navController.navigate("rpalcourse")
                                "handgun" -> navController.navigate("handgunsafety")
                                "pistolqual" -> navController.navigate("pistolqual")
                                else -> navController.navigate("courses")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "OUR FACILITIES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90EE90),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            FeatureIcon("🎯", "Pistol") { navController.navigate("pistolrange") }
            FeatureIcon("🔫", "Rifle") { navController.navigate("riflerange") }
            FeatureIcon("🏹", "Archery") { navController.navigate("archeryrange") }
            FeatureIcon("🥏", "Trap") { navController.navigate("traprange") }
        }

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(title = "📍 CONTACT US") {
            Text(kdfgcData?.clubInfo?.name ?: "Kelowna and District Fish & Game Club", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(kdfgcData?.clubInfo?.address ?: "2319 Rifle Road, Kelowna, BC", color = Color(0xFFCCCCCC), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📧 ${kdfgcData?.clubInfo?.email ?: "info@kdfgc.org"}", color = Color(0xFF90EE90), fontSize = 13.sp)
            Text("📞 ${kdfgcData?.clubInfo?.phone ?: "(250) 762-2111"}", color = Color(0xFF90EE90), fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
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
                    CourseInfoRow("📅 Date", selectedCourse!!.date)
                    CourseInfoRow("🕐 Time", selectedCourse!!.time)
                    CourseInfoRow("📍 Location", selectedCourse!!.location)
                    CourseInfoRow("💰 Cost", selectedCourse!!.cost)
                    CourseInfoRow("👥 Seats Left", "${selectedCourse!!.seatsRemaining}/${selectedCourse!!.classSize}")
                    CourseInfoRow("👨‍🏫 Instructor", selectedCourse!!.instructorName)
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
fun CourseCard(course: Course, onRegisterClick: () -> Unit, onContactClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(course.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(course.cost, color = Color(0xFF90EE90), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(course.description, color = Color(0xFF888888), fontSize = 12.sp, lineHeight = 16.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("📅 ${course.date}", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                    Text("🕐 ${course.time}", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("📍 ${course.location}", color = Color(0xFFCCCCCC), fontSize = 12.sp)
                    Text("👥 ${course.seatsRemaining}/${course.classSize} seats", color = if (course.seatsRemaining <= 3) Color(0xFFFF6B6B) else Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRegisterClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), enabled = course.seatsRemaining > 0) {
                    Icon(Icons.Default.HowToReg, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (course.seatsRemaining > 0) "REGISTER" else "FULL", fontSize = 12.sp)
                }
                OutlinedButton(onClick = onContactClick, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp), tint = Color(0xFF90EE90))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CONTACT", fontSize = 12.sp, color = Color(0xFF90EE90))
                }
            }
        }
    }
}

@Composable
fun CourseInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF888888), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
fun NavTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(if (selected) Modifier.background(Color(0xFF007236)) else Modifier)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = if (selected) Color.White else Color(0xFFAAAAAA), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CTAButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)), shape = RoundedCornerShape(25.dp), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
        Text(text = text, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(colors = listOf(Color(0xFF007236), Color(0xFF009944)))).padding(12.dp)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(modifier = Modifier.padding(14.dp), content = content)
        }
    }
}

@Composable
fun ClickableEventItem(title: String, details: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 6.dp)) {
        Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "$details →", color = Color(0xFF888888), fontSize = 11.sp)
    }
    HorizontalDivider(color = Color(0xFF333333))
}

@Composable
fun CourseChip(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF2A2A2A)).clickable { onClick() }.padding(horizontal = 14.dp, vertical = 8.dp)) {
        Text(text = text, color = Color(0xFF90EE90), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun FeatureIcon(emoji: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2A2A2A))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(text = emoji, fontSize = 26.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color(0xFF90EE90),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}