package com.example.easylaw.authscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ════════════════════════════════════════════════════════════════════════════
//  DESIGN TOKENS  (shared with HomeScreen)
// ════════════════════════════════════════════════════════════════════════════
private val BgPage       = Color(0xFFF0F4FA)
private val BlueAccent   = Color(0xFF1A6CF0)
private val BlueDark     = Color(0xFF0A2F78)
private val BlueMid      = Color(0xFF2451B3)
private val BlueLight    = Color(0xFFE8F0FE)
private val RedAccent    = Color(0xFFDC2626)
private val CardWhite    = Color(0xFFFFFFFF)
private val TextPrimary  = Color(0xFF0C1929)
private val TextSecondary= Color(0xFF546378)
private val TextMuted    = Color(0xFF9AACBE)
private val DividerColor = Color(0xFFE6EDF5)

// ════════════════════════════════════════════════════════════════════════════
//  LOGIN SCREEN
//
//  API INTEGRATION:
//  ────────────────
//  Replace the fake delay + snackbar block with your real auth call:
//
//    authViewModel.login(phone, password)
//        .onSuccess { navController.navigate("BottomNavigation") { popUpTo("login") { inclusive = true } } }
//        .onFailure { error = it.message ?: "Login failed. Please try again." }
//
//  Firebase Auth example:
//    FirebaseAuth.getInstance()
//        .signInWithEmailAndPassword(phone, password)   // or phone auth flow
//        .addOnSuccessListener { ... }
//        .addOnFailureListener { error = it.localizedMessage ?: "Login failed." }
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun LoginScreen(navController: NavHostController) {

    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var error           by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading       by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BgPage
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {

            // ── Hero gradient header ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(BlueDark, BlueMid, BlueAccent),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end   = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // App icon circle
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector    = Icons.Outlined.Balance,
                            contentDescription = "EasyLaw",
                            tint           = Color.White,
                            modifier       = Modifier.size(34.dp)
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text       = "EasyLaw",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = "Your legal rights, made simple",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.72f)
                    )
                }
            }

            // ── Form card ───────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-24).dp),
                shape  = RoundedCornerShape(24.dp),
                color  = CardWhite,
                shadowElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    Text(
                        text       = "Sign In",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text     = "Enter your credentials to continue",
                        fontSize = 13.sp,
                        color    = TextMuted
                    )

                    Spacer(Modifier.height(24.dp))

                    // Phone field
                    Text(
                        text       = "Mobile Number",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextSecondary,
                        modifier   = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value         = phone,
                        onValueChange = { phone = it; error = "" },
                        placeholder   = { Text("e.g. 01700000000", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(
                                Icons.Outlined.Phone,
                                contentDescription = null,
                                tint     = if (phone.isNotEmpty()) BlueAccent else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine      = true,
                        shape           = RoundedCornerShape(14.dp),
                        colors          = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = BlueAccent,
                            unfocusedBorderColor = DividerColor,
                            focusedTextColor     = TextPrimary,
                            unfocusedTextColor   = TextPrimary,
                            cursorColor          = BlueAccent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password field
                    Text(
                        text       = "Password",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextSecondary,
                        modifier   = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it; error = "" },
                        placeholder   = { Text("Enter your password", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = null,
                                tint     = if (password.isNotEmpty()) BlueAccent else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector        = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint               = TextMuted,
                                    modifier           = Modifier.size(20.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine      = true,
                        shape           = RoundedCornerShape(14.dp),
                        colors          = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = BlueAccent,
                            unfocusedBorderColor = DividerColor,
                            focusedTextColor     = TextPrimary,
                            unfocusedTextColor   = TextPrimary,
                            cursorColor          = BlueAccent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Inline error message
                    if (error.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = RedAccent.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text     = error,
                                color    = RedAccent,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Forgot password link (right-aligned)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text       = "Forgot password?",
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = BlueAccent,
                            modifier   = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = ripple(color = BlueAccent)
                                ) { navController.navigate("forgot_password") }
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Sign In button
                    Button(
                        onClick = {
                            when {
                                phone.isBlank() || password.isBlank() -> {
                                    error = "Please fill in all fields."
                                }
                                !phone.all { it.isDigit() } || phone.length < 10 -> {
                                    error = "Please enter a valid mobile number."
                                }
                                else -> {
                                    scope.launch {
                                        isLoading = true
                                        delay(100) // replace with real auth call
                                        isLoading = false
                                        snackbarHostState.showSnackbar("Login successful 🎉")
                                        navController.navigate("BottomNavigation") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape   = RoundedCornerShape(14.dp),
                        colors  = ButtonDefaults.buttonColors(
                            containerColor         = BlueAccent,
                            disabledContainerColor = BlueAccent.copy(alpha = 0.5f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier  = Modifier.size(22.dp),
                                color     = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text       = "Sign In",
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }

            // ── Register link ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = "Don't have an account?",
                    fontSize = 13.sp,
                    color    = TextSecondary
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text       = "Register",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = BlueAccent,
                    modifier   = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = ripple(color = BlueAccent)
                        ) { navController.navigate("register") }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }
        }
    }
}