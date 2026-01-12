package com.example.kdfgc_capstone

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kdfgc_capstone.ui.theme.KDFGC_CapstoneTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

val Context.dataStore by preferencesDataStore("user_prefs")

enum class Screen {
    Home, Events, Membership
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KDFGC_CapstoneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppMainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScreen() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            Screen.Home -> "Kelowna Fish & Game Club"
                            Screen.Events -> "Events"
                            Screen.Membership -> "Membership"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    if (currentScreen != Screen.Home) {
                        IconButton(onClick = { currentScreen = Screen.Home }) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_media_previous),
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E7D32),
                            Color(0xFFE8F5E9)
                        )
                    )
                )
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    onEventsClick = { currentScreen = Screen.Events },
                    onMembershipClick = { currentScreen = Screen.Membership },
                    onContactClick = { /* TODO: Implement contact action */ }
                )
                Screen.Events -> EventsScreen()
                Screen.Membership -> MembershipScreen()
            }
        }
    }
}

data class Announcement(val title: String, val date: String, val content: String)
data class Event(val title: String, val date: String, val location: String)

@Composable
fun HomeScreen(
    onEventsClick: () -> Unit,
    onMembershipClick: () -> Unit,
    onContactClick: () -> Unit
) {
    val announcements = remember {
        listOf(
            Announcement("New Safety Protocols", "Jan 10, 2026", "Please review updated safety protocols."),
            Announcement("Annual General Meeting", "Feb 5, 2026", "Join us for the AGM at 7 PM."),
            Announcement("Range Maintenance", "Jan 20, 2026", "Range will be closed for maintenance.")
        )
    }
    val upcomingEvents = remember {
        listOf(
            Event("Winter Shooting Competition", "Jan 25, 2026", "Main Range"),
            Event("Youth Archery Camp", "Feb 15, 2026", "Archery Range"),
            Event("Family Fishing Day", "Mar 10, 2026", "Lakefront")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.generated_image_2026_01_12),
                contentDescription = "Kelowna Fish & Game Club",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 16.dp)
                    .background(
                        color = Color(0xFF1B5E20),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }
        item {
            Text(
                text = "Welcome to Kelowna Fish & Game Club",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Connecting outdoor enthusiasts, families, and conservationists for over 50 years.",
                fontSize = 16.sp,
                color = Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 24.dp),
                lineHeight = 22.sp
            )
        }
        item {
            SectionTitle("Announcements")
            announcements.forEach { announcement ->
                AnnouncementCard(announcement)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Upcoming Events")
            upcomingEvents.forEach { event ->
                EventCard(event)
            }
            Button(
                onClick = onEventsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Filled.Event, contentDescription = "View All Events", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("View All Events", fontSize = 18.sp)
            }
        }
        item {
            SectionTitle("Membership")
            Text(
                "Join or renew your membership to support the club and enjoy member benefits.",
                fontSize = 16.sp,
                color = Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Button(
                onClick = onMembershipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Filled.Group, contentDescription = "Membership", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Membership", fontSize = 18.sp)
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Range Status")
            Text(
                "Main Range: Open\nArchery Range: Closed for Maintenance\nHours: 8:00 AM - 8:00 PM",
                fontSize = 16.sp,
                color = Color(0xFF388E3C)
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Contact & Quick Links")
            ContactInfoCard(onContactClick)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Footer()
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1B5E20),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(announcement.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
            Text(announcement.date, fontSize = 12.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            Text(announcement.content, fontSize = 14.sp, color = Color(0xFF1B5E20), maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.CalendarToday, contentDescription = "Event Date", tint = Color(0xFF2E7D32))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B5E20))
                Text("${event.date} • ${event.location}", fontSize = 14.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun ContactInfoCard(onContactClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onContactClick() }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact Us", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color(0xFF1B5E20))
                Spacer(Modifier.width(8.dp))
                Text("info@kdfgc.org", fontSize = 14.sp, color = Color(0xFF1B5E20))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Phone, contentDescription = "Phone", tint = Color(0xFF1B5E20))
                Spacer(Modifier.width(8.dp))
                Text("(250) 555-1234", fontSize = 14.sp, color = Color(0xFF1B5E20))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Place, contentDescription = "Address", tint = Color(0xFF1B5E20))
                Spacer(Modifier.width(8.dp))
                Text("123 Fish & Game Rd, Kelowna, BC", fontSize = 14.sp, color = Color(0xFF1B5E20))
            }
        }
    }
}

@Composable
fun Footer() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("© 2026 Kelowna Fish & Game Club", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Privacy Policy", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
            Text("Terms of Service", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
            Text("Facebook", color = Color.Blue, modifier = Modifier.clickable { /* TODO */ })
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upcoming Club Events",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        val url = URL("https://dog.ceo/api/breeds/image/random")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connect()
                        val response = connection.inputStream.bufferedReader().readText()
                        val image = JSONObject(response).getString("message")
                        imageUrl = image
                        isLoading = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Load Random Event Photo", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else if (imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Random event photo",
                    modifier = Modifier.size(220.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Check back soon for more club events, tournaments, and family days!",
                color = Color(0xFF388E3C)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userNameKey = stringPreferencesKey("user_name")
    var userName by remember { mutableStateOf("") }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        val savedName = context.dataStore.data.map { it[userNameKey] ?: "" }.first()
        userName = savedName
        textFieldValue = TextFieldValue(savedName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Club Membership",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF388E3C)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Your Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        context.dataStore.edit { prefs ->
                            prefs[userNameKey] = textFieldValue.text
                        }
                        userName = textFieldValue.text
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("Save Name", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (userName.isNotEmpty()) {
                Text("Welcome, $userName!", color = Color(0xFF1976D2))
            }
        }
    }
}
