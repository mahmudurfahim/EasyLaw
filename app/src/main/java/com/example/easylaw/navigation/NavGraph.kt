package com.example.easylaw.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easylaw.screens.DashboardScreen
import com.example.easylaw.screens.LoginScreen
import com.example.easylaw.screens.RegisterScreen

@Composable
fun EasyLawApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 🔐 Login Screen
        composable("login") {
            LoginScreen(navController)
        }

        // 📝 Register Screen
        composable("register") {
            RegisterScreen(navController)
        }

        // 🏠 Dashboard (User only, no role)
        composable("dashboard") {
            DashboardScreen()
        }
    }
}