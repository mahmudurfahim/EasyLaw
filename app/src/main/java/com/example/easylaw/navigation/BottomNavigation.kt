package com.example.easylaw.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.easylaw.explorescreens.ExploreScreen
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easylaw.homescreens.HomeScreen
import com.example.easylaw.lawyerscreens.LawyerScreen
import com.example.easylaw.morescreens.MoreScreen

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home    : BottomNavItem("home",    "Home",    Icons.Outlined.Home)
    object Ainjibi : BottomNavItem("ainjibi", "Lawyers", Icons.Outlined.Person)
    object Explore : BottomNavItem("explore", "Explore", Icons.Outlined.AccountBalance)
    object More    : BottomNavItem("more",    "More",    Icons.Outlined.GridView)
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
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
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
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = Color(0xFF1E6FD9),
                            selectedTextColor   = Color(0xFF1E6FD9),
                            indicatorColor      = Color(0xFFE3F0FF),
                            unselectedIconColor = Color(0xFF9AAABB),
                            unselectedTextColor = Color(0xFF9AAABB)
                        )
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
            composable(BottomNavItem.Home.route)    { HomeScreen() }
            composable(BottomNavItem.Ainjibi.route) { LawyerScreen() }
            composable(BottomNavItem.Explore.route) { ExploreScreen() }
            composable(BottomNavItem.More.route)    { MoreScreen() }
        }
    }
}