package com.example.easylaw.lawyerscreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage  // ✅ CORRECT Coil import — not a local stub

// ════════════════════════════════════════════════════════════════════════════
//  COLORS
// ════════════════════════════════════════════════════════════════════════════
private val BlueLight     = Color(0xFFE3F2FD)
private val BlueCard      = Color(0xFFBBDEFB)
private val BlueAccent    = Color(0xFF2196F3)
private val BlueDark      = Color(0xFF1565C0)
private val TextPrimary   = Color(0xFF0D1B2A)
private val TextSecondary = Color(0xFF546E7A)

// ════════════════════════════════════════════════════════════════════════════
//  DATA MODEL
//
//  API FIELD MAPPING:
//  ┌──────────────────┬───────────────────────────────────────────────────┐
//  │ id               │ Firestore doc ID or REST primary key              │
//  │ name             │ Full name string                                  │
//  │ specialization   │ Must match a value in lawCategories list          │
//  │ experience       │ e.g. "12 yrs"                                     │
//  │ location         │ City name                                         │
//  │ fee              │ e.g. "৳ 2,000/hr"                                │
//  │ imageUrl         │ Full HTTPS URL — Firebase Storage or any CDN      │
//  └──────────────────┴───────────────────────────────────────────────────┘
// ════════════════════════════════════════════════════════════════════════════
data class Lawyer(
    val id: String,
    val name: String,
    val specialization: String,
    val experience: String,
    val location: String,
    val fee: String,
    val imageUrl: String
)

// ════════════════════════════════════════════════════════════════════════════
//  LAW CATEGORIES
// ════════════════════════════════════════════════════════════════════════════
val lawCategories = listOf(
    "Criminal Law",
    "Family Law",
    "Corporate Law",
    "Civil Law",
    "Property Law",
    "Labour Law",
    "Tax Law",
    "Constitutional Law"
)

// ════════════════════════════════════════════════════════════════════════════
//  SAMPLE DATA  —  delete when connecting real API
// ════════════════════════════════════════════════════════════════════════════
val sampleLawyers = listOf(
    Lawyer("1", "Adv. Rahim Uddin",   "Criminal Law",  "12 yrs", "Dhaka",      "৳ 2,000/hr", "https://i.pravatar.cc/150?img=11"),
    Lawyer("2", "Adv. Nasrin Akter",  "Family Law",    "8 yrs",  "Chittagong", "৳ 1,500/hr", "https://i.pravatar.cc/150?img=20"),
    Lawyer("3", "Adv. Kamal Hossain", "Corporate Law", "15 yrs", "Dhaka",      "৳ 3,500/hr", "https://i.pravatar.cc/150?img=33"),
    Lawyer("4", "Adv. Sumaiya Islam", "Civil Law",     "6 yrs",  "Sylhet",     "৳ 1,200/hr", "https://i.pravatar.cc/150?img=47"),
    Lawyer("5", "Adv. Tariq Mahmud",  "Property Law",  "10 yrs", "Dhaka",      "৳ 2,500/hr", "https://i.pravatar.cc/150?img=53"),
    Lawyer("6", "Adv. Farida Begum",  "Labour Law",    "9 yrs",  "Rajshahi",   "৳ 1,800/hr", "https://i.pravatar.cc/150?img=45"),
)

// ════════════════════════════════════════════════════════════════════════════
//  SCREEN
//
//  API INTEGRATION:
//  ─────────────────
//  Replace default param with ViewModel state:
//    val lawyers by viewModel.lawyers.collectAsState()
//
//  Firestore mapping in ViewModel:
//    firestore.collection("lawyers").get()
//        .addOnSuccessListener { result ->
//            _lawyers.value = result.map { doc ->
//                Lawyer(
//                    id             = doc.id,
//                    name           = doc.getString("name") ?: "",
//                    specialization = doc.getString("specialization") ?: "",
//                    experience     = doc.getString("experience") ?: "",
//                    location       = doc.getString("location") ?: "",
//                    fee            = doc.getString("fee") ?: "",
//                    imageUrl       = doc.getString("imageUrl") ?: ""
//                )
//            }
//        }
// ════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerScreen(
    lawyers: List<Lawyer> = sampleLawyers,
    onLawyerClick: (Lawyer) -> Unit = {}
) {
    var searchQuery      by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val allChips = listOf("All") + lawCategories

    val filtered = remember(searchQuery, selectedCategory, lawyers) {
        lawyers
            .filter { selectedCategory == "All" || it.specialization == selectedCategory }
            .filter {
                searchQuery.isBlank() ||
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.specialization.contains(searchQuery, ignoreCase = true) ||
                        it.location.contains(searchQuery, ignoreCase = true)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Gradient Header ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(BlueAccent, BlueDark)))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = "আইনজীবী",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Find your trusted legal advisor",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(16.dp))

                    // Search Bar
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Search by name, specialty, location...",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor        = Color.White,
                                unfocusedTextColor      = Color.White,
                                focusedContainerColor   = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor   = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor             = Color.White
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ── Horizontal Category Chips ────────────────────────────────────
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                allChips.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isSelected) BlueAccent else Color.White,
                        shadowElevation = if (isSelected) 4.dp else 1.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = BlueAccent)
                            ) { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            // ── Lawyer List ──────────────────────────────────────────────────
            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.SearchOff,
                            contentDescription = null,
                            tint = BlueAccent.copy(alpha = 0.4f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No lawyers found",
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 20.dp, end = 20.dp, bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filtered, key = { it.id }) { lawyer ->
                        LawyerCard(lawyer = lawyer, onClick = { onLawyerClick(lawyer) })
                    }
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  LAWYER CARD
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun LawyerCard(lawyer: Lawyer, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "card_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = BlueAccent.copy(alpha = 0.15f),
                spotColor = BlueAccent.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = BlueAccent),
                onClick = {
                    pressed = true
                    onClick()
                }
            ),
        color = Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Avatar + Name + Specialization ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Lawyer Photo ─────────────────────────────────────────────
                // Uses Coil's AsyncImage for async network loading.
                // Shows a Person icon fallback if imageUrl is empty or fails.
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(BlueCard),
                    contentAlignment = Alignment.Center
                ) {
                    if (lawyer.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = lawyer.imageUrl,
                            contentDescription = lawyer.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(62.dp)
                                .clip(CircleShape),
                            error = androidx.compose.ui.res.painterResource(
                                android.R.drawable.ic_menu_gallery
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = BlueAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lawyer.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(5.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = BlueAccent.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = lawyer.specialization,
                            fontSize = 12.sp,
                            color = BlueAccent,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // ── Divider ──────────────────────────────────────────────────────
            HorizontalDivider(color = BlueLight, thickness = 1.dp)

            // ── Experience + Location + Fee ──────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoChip(icon = Icons.Outlined.WorkHistory, label = lawyer.experience)
                Spacer(Modifier.width(8.dp))
                InfoChip(icon = Icons.Outlined.LocationOn, label = lawyer.location)
                Spacer(Modifier.weight(1f))
                Text(
                    text = lawyer.fee,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueDark
                )
            }

            // ── Book Consultation Button ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BlueAccent, BlueDark)))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = Color.White),
                        onClick = onClick
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Book Consultation",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  INFO CHIP
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = BlueCard.copy(alpha = 0.6f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BlueAccent,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}