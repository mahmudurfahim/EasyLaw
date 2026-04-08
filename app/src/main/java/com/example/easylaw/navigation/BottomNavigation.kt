package com.example.easylaw.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easylaw.authscreens.*
import com.example.easylaw.explorescreens.ExploreScreen
import com.example.easylaw.homescreens.HomeScreen
import com.example.easylaw.lawyerscreens.LawyerScreen
import com.example.easylaw.morescreens.MoreScreen

sealed class BottomNavItem(val route: String, val label: String) {
    object Home : BottomNavItem("home", "হোম")
    object Ainjibi : BottomNavItem("ainjibi", "আইনজীবী")
    object Explore : BottomNavItem("explore", "এক্সপ্লোর")
    object More : BottomNavItem("more", "মোর")
}

@Composable
fun DashboardWithBottomNav() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Ainjibi,
        BottomNavItem.Explore,
        BottomNavItem.More
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        label = { Text(item.label) },
                        icon = { /* Add icons here if needed */ }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Ainjibi.route) { LawyerScreen() }
            composable(BottomNavItem.Explore.route) { ExploreScreen() }
            composable(BottomNavItem.More.route) { MoreScreen() }
        }
    }
}