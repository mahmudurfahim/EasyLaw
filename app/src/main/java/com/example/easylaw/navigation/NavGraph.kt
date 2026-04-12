package com.example.easylaw.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easylaw.authscreens.ForgotPasswordScreen
import com.example.easylaw.authscreens.LoginScreen
import com.example.easylaw.authscreens.RegisterScreen

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

        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }


        composable("BottomNavigation") {
            BottomNavigation()
        }

    }
}