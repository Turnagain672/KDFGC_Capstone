package com.example.capstone2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstone2.ui.theme.Capstone2Theme
import com.example.capstone2.ui.screens.*
import com.example.capstone2.data.WorkManagerHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Schedule background data sync every 6 hours
        WorkManagerHelper.schedulePeriodicSync(this)

        setContent {
            Capstone2Theme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userViewModel: UserViewModel = viewModel()

    val bottomNavItems = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("events", "Events", Icons.Default.Event),
        BottomNavItem("forum", "Forum", Icons.Default.Forum),
        BottomNavItem("rangelog", "Range Log", Icons.Default.TrackChanges),
        BottomNavItem("profile", "Profile", Icons.Default.Person)
    )

    val showBottomNav = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController = navController) }
            composable("events") { EventsScreen(navController = navController) }
            composable("forum") { ForumScreen(navController = navController) }
            composable("rangelog") { RangeLogScreen(navController = navController) }
            composable("profile") { ProfileScreen(navController = navController, userViewModel = userViewModel) }
            composable("editprofile") { EditProfileScreen(navController = navController) }
            composable("notifications") { NotificationsScreen(navController = navController) }
            composable("privacy") { PrivacyScreen(navController = navController) }
            composable("renew") { RenewScreen(navController = navController) }
            composable("documents") { DocumentsScreen(navController = navController) }
            composable("help") { HelpScreen(navController = navController) }
            composable("login") { LoginScreen(navController = navController, userViewModel = userViewModel) }
            composable("join") { JoinScreen(navController = navController) }
            composable("ranges") { RangesScreen(navController = navController) }
            composable("courses") { CoursesScreen(navController = navController) }
            composable("about") { AboutScreen(navController = navController) }
            composable("palcourse") { PALCourseScreen(navController = navController) }
            composable("rpalcourse") { RPALCourseScreen(navController = navController) }
            composable("handgunsafety") { HandgunSafetyScreen(navController = navController) }
            composable("pistolqual") { PistolQualScreen(navController = navController) }
            composable("pistolrange") { PistolRangeScreen(navController = navController) }
            composable("riflerange") { RifleRangeScreen(navController = navController) }
            composable("archeryrange") { ArcheryRangeScreen(navController = navController) }
            composable("traprange") { TrapRangeScreen(navController = navController) }
            composable("register/{eventName}") { backStackEntry ->
                val eventName = backStackEntry.arguments?.getString("eventName") ?: "Event"
                RegistrationScreen(navController = navController, eventName = eventName)
            }
            composable("store") { StoreScreen(navController = navController) }
            composable("admin") { AdminScreen(navController = navController) }
            composable("checkout/{itemName}/{price}") { backStackEntry ->
                val itemName = backStackEntry.arguments?.getString("itemName") ?: "Item"
                val price = backStackEntry.arguments?.getString("price") ?: "0.00"
                CheckoutScreen(navController = navController, itemName = itemName, price = price, userViewModel = userViewModel)
            }
            composable("myaccount") { MyAccountScreen(navController = navController, userViewModel = userViewModel) }
            composable("adminpanel") { AdminPanelScreen(navController = navController, userViewModel = userViewModel) }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)