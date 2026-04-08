package com.example.easylaw.authscreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
fun RegisterScreen(navController: NavHostController) {

    // OTP States
    var showOtpSheet by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }

    // Form States
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // UI States
    var passwordVisible by remember { mutableStateOf(false) }
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

            Text("Register", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(20.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = "" },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; error = "" },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = "" },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // Error message
            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Register Button
            Button(
                onClick = {
                    when {
                        name.isBlank() || phone.isBlank() || password.isBlank() -> {
                            error = "Please fill all fields"
                        }

                        // Bangladesh phone validation (01XXXXXXXXX)
                        !phone.startsWith("01") || phone.length != 11 || !phone.all { it.isDigit() } -> {
                            error = "Enter valid phone number (01XXXXXXXXX)"
                        }

                        // Password validation
                        password.length < 6 -> {
                            error = "Password must be at least 6 characters"
                        }

                        else -> {
                            scope.launch {
                                isLoading = true
                                delay(1000)
                                isLoading = false

                                // 👉 Show OTP sheet instead of navigating
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
                    Text("Register")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("Already have an account? Login")
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
                                    snackbarHostState.showSnackbar("Registration Successful 🎉")

                                    navController.navigate("dashboard") {
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