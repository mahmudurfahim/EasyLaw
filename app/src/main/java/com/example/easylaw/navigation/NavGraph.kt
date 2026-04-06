package com.example.easylaw.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.easylaw.screens.*

@Composable
fun EasyLawApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("dashboard/{role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "Guest"
            DashboardScreen(role)
        }
    }
}