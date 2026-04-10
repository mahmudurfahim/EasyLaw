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
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ════════════════════════════════════════════════════════════════════════════
//  DESIGN TOKENS  (shared across all auth screens)
// ════════════════════════════════════════════════════════════════════════════
private val BgPage        = Color(0xFFF0F4FA)
private val BlueAccent    = Color(0xFF1A6CF0)
private val BlueDark      = Color(0xFF0A2F78)
private val BlueMid       = Color(0xFF2451B3)
private val BlueLight     = Color(0xFFE8F0FE)
private val GreenAccent   = Color(0xFF0F9E57)
private val RedAccent     = Color(0xFFDC2626)
private val CardWhite     = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0C1929)
private val TextSecondary = Color(0xFF546378)
private val TextMuted     = Color(0xFF9AACBE)
private val DividerColor  = Color(0xFFE6EDF5)

// ════════════════════════════════════════════════════════════════════════════
//  REGISTER SCREEN
//
//  API INTEGRATION:
//  ────────────────
//  "Create Account" button → send OTP after saving draft user:
//    authViewModel.sendOtp(phone)
//        .onSuccess { showOtpSheet = true }
//        .onFailure { error = it.message ?: "Failed to send OTP." }
//
//  "Verify OTP" button → create user account:
//    authViewModel.verifyOtpAndRegister(name, phone, password, otp)
//        .onSuccess {
//            snackbarHostState.showSnackbar("Registration successful 🎉")
//            navController.navigate("BottomNavigation") { popUpTo("login") { inclusive = true } }
//        }
//        .onFailure { error = it.message ?: "Invalid OTP. Please try again." }
// ════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {

    var showOtpSheet    by remember { mutableStateOf(false) }
    var otp             by remember { mutableStateOf("") }
    var name            by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var error           by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading       by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = BgPage
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {

            // ── Gradient hero header ─────────────────────────────────────
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
                    .padding(horizontal = 24.dp, vertical = 36.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Back button
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(color = Color.White)
                                ) { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint     = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // App icon circle
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Balance,
                            contentDescription = null,
                            tint     = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text          = "Create Account",
                        fontSize      = 24.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        color         = Color.White,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text       = "Join EasyLaw and know your rights",
                        fontSize   = 13.sp,
                        color      = Color.White.copy(alpha = 0.72f),
                        textAlign  = TextAlign.Center
                    )
                }
            }

            // ── Form card ───────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-24).dp),
                shape           = RoundedCornerShape(24.dp),
                color           = CardWhite,
                shadowElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    Text(
                        text          = "Your Details",
                        fontSize      = 18.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = TextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                    Text(
                        text      = "Fill in the fields below to get started",
                        fontSize  = 12.sp,
                        color     = TextMuted,
                        lineHeight = 17.sp
                    )

                    Spacer(Modifier.height(22.dp))

                    // Full name field
                    Text(
                        text       = "Full Name",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextSecondary,
                        modifier   = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it; error = "" },
                        placeholder   = { Text("e.g. Rahim Uddin", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint     = if (name.isNotEmpty()) BlueAccent else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        singleLine = true,
                        enabled    = !isLoading,
                        shape      = RoundedCornerShape(14.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = BlueAccent,
                            unfocusedBorderColor = DividerColor,
                            focusedTextColor     = TextPrimary,
                            unfocusedTextColor   = TextPrimary,
                            cursorColor          = BlueAccent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

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
                        placeholder   = { Text("01XXXXXXXXX", color = TextMuted, fontSize = 14.sp) },
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
                        enabled         = !isLoading,
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
                        placeholder   = { Text("At least 6 characters", color = TextMuted, fontSize = 14.sp) },
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
                        enabled         = !isLoading,
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

                    // Inline error
                    if (error.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
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

                    Spacer(Modifier.height(22.dp))

                    // Create Account button
                    Button(
                        onClick = {
                            when {
                                name.isBlank() || phone.isBlank() || password.isBlank() -> {
                                    error = "Please fill in all fields."
                                }
                                !phone.startsWith("01") || phone.length != 11 || !phone.all { it.isDigit() } -> {
                                    error = "Enter a valid number in the format 01XXXXXXXXX."
                                }
                                password.length < 6 -> {
                                    error = "Password must be at least 6 characters."
                                }
                                else -> {
                                    scope.launch {
                                        isLoading = true
                                        delay(1000) // replace with real OTP send call
                                        isLoading = false
                                        showOtpSheet = true
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
                                modifier    = Modifier.size(22.dp),
                                color       = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text          = "Create Account",
                                fontSize      = 15.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = Color.White,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }

            // ── Sign in link ─────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = "Already have an account?",
                    fontSize = 13.sp,
                    color    = TextSecondary
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text       = "Sign In",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = BlueAccent,
                    modifier   = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = ripple(color = BlueAccent)
                        ) { navController.popBackStack() }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }
        }

        // ── OTP Bottom Sheet ─────────────────────────────────────────────
        if (showOtpSheet) {
            ModalBottomSheet(
                onDismissRequest = { showOtpSheet = false },
                containerColor   = CardWhite,
                shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sheet handle
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(DividerColor)
                    )
                    Spacer(Modifier.height(20.dp))

                    // OTP icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Pin,
                            contentDescription = null,
                            tint     = BlueAccent,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text          = "Verify Your Number",
                        fontSize      = 20.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = TextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text       = "A 6-digit code was sent to\n$phone",
                        fontSize   = 13.sp,
                        color      = TextMuted,
                        textAlign  = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text       = "Verification Code",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextSecondary,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value         = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        placeholder   = { Text("6-digit OTP", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(
                                Icons.Outlined.Pin,
                                contentDescription = null,
                                tint     = if (otp.isNotEmpty()) BlueAccent else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                    Spacer(Modifier.height(22.dp))

                    // Verify button
                    Button(
                        onClick = {
                            if (otp.length == 6 && otp.all { it.isDigit() }) {
                                showOtpSheet = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Registration successful 🎉")
                                    navController.navigate("BottomNavigation") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape  = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenAccent)
                    ) {
                        Text(
                            text          = "Verify & Complete Registration",
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // Resend link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text     = "Didn't receive the code?",
                            fontSize = 13.sp,
                            color    = TextSecondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text       = "Resend",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color      = BlueAccent,
                            modifier   = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = ripple(color = BlueAccent)
                                ) { /* trigger resend OTP */ }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}