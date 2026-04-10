package com.example.easylaw.morescreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.Logout
import androidx.compose.ui.draw.scale

// ─── Colors ───────────────────────────────────────────────────────────────────
private val YellowLight   = Color(0xFFE3F2FD)
private val YellowCard    = Color(0xFFBBDEFB)
private val YellowAccent  = Color(0xFF2196F3)
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF5C5C7A)
private val RedLogout     = Color(0xFFE53935)
private val RedLogoutBg   = Color(0xFFFFEBEB)

// ─── Data ─────────────────────────────────────────────────────────────────────
data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val subtitle: String = ""
)

@Composable
fun MoreScreen(
    onProfileClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val menuItems = listOf(
        MenuItem(Icons.Outlined.Person,      "Profile",            "View & edit your info"),
        MenuItem(Icons.Outlined.Info,        "About Us",           "Learn about EasyLaw"),
        MenuItem(Icons.Outlined.Shield,      "Privacy Policy",     "How we protect you"),
        MenuItem(Icons.Outlined.Description, "Terms & Conditions", "Usage guidelines"),
        MenuItem(Icons.Outlined.HeadsetMic,  "Contact Us",         "We're here to help"),
    )
    val actions = listOf(
        onProfileClick, onAboutClick, onPrivacyClick, onTermsClick, onContactClick
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(Modifier.height(24.dp))

            // ── Header ──────────────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(YellowAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "My Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Manage your preferences",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Menu Items ──────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                menuItems.forEachIndexed { index, item ->
                    MoreMenuItem(
                        icon = item.icon,
                        label = item.label,
                        subtitle = item.subtitle,
                        onClick = actions[index]
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Divider ─────────────────────────────────────────────────────
            HorizontalDivider(
                color = YellowAccent.copy(alpha = 0.4f),
                thickness = 1.dp
            )

            Spacer(Modifier.height(20.dp))

            // ── Logout Button ───────────────────────────────────────────────
            LogoutButton(onClick = onLogoutClick)
        }
    }
}

// ─── Menu Item Card ────────────────────────────────────────────────────────────
@Composable
fun MoreMenuItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (pressed) 0.dp else 3.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = YellowAccent.copy(alpha = 0.3f),
                spotColor = YellowAccent.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = YellowAccent),
                onClick = {
                    pressed = true
                    onClick()
                }
            ),
        color = YellowCard,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(YellowAccent.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Chevron
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Logout Button ─────────────────────────────────────────────────────────────
@Composable
fun LogoutButton(onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "logout_scale"
    )

    Surface(
        modifier = Modifier.run {
            val clickable = fillMaxWidth()
                .scale(scale)
                .shadow(
                    elevation = if (pressed) 0.dp else 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = RedLogout.copy(alpha = 0.2f),
                    spotColor = RedLogout.copy(alpha = 0.15f)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = RedLogout),
                    onClick = {
                        pressed = true
                        onClick()
                    }
                )
            clickable
        },
        color = RedLogoutBg,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RedLogout.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout",
                    tint = RedLogout,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Log Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RedLogout
                )
                Text(
                    text = "Sign out of your account",
                    fontSize = 12.sp,
                    color = RedLogout.copy(alpha = 0.65f)
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = RedLogout.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}