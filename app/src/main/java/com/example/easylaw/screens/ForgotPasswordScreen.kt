package com.example.easylaw.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    var phone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    var showOtpSheet by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Forgot Password", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(20.dp))

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; error = "" },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it; error = "" },
                label = { Text("New Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        phone.isBlank() || newPassword.isBlank() -> {
                            error = "Please fill all fields"
                        }
                        !phone.startsWith("01") || phone.length != 11 || !phone.all { it.isDigit() } -> {
                            error = "Enter valid phone number (01XXXXXXXXX)"
                        }
                        newPassword.length < 6 -> {
                            error = "Password must be at least 6 characters"
                        }
                        else -> {
                            scope.launch {
                                isLoading = true
                                delay(1000)
                                isLoading = false

                                showOtpSheet = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Send OTP")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("Back to Login")
            }
        }

        // 🔐 OTP Bottom Sheet
        if (showOtpSheet) {
            ModalBottomSheet(
                onDismissRequest = { showOtpSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text("Enter OTP", style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text("OTP") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (otp.length == 6 && otp.all { it.isDigit() }) {
                                showOtpSheet = false

                                scope.launch {
                                    snackbarHostState.showSnackbar("Password Reset Successful 🎉")

                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify OTP")
                    }
                }
            }
        }
    }
}