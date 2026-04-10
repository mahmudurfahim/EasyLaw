package com.example.easylaw.homescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ════════════════════════════════════════════════════════════════════════════
//  COLORS
// ════════════════════════════════════════════════════════════════════════════
private val BgPage        = Color(0xFFF4F7FB)
private val BlueAccent    = Color(0xFF1E6FD9)
private val BlueDark      = Color(0xFF0D3E87)
private val BlueLight     = Color(0xFFE3F0FF)
private val BlueCard      = Color(0xFFBBDEFB)
private val GreenAccent   = Color(0xFF18A558)
private val GreenLight    = Color(0xFFE6F7EE)
private val OrangeAccent  = Color(0xFFE07B39)
private val OrangeLight   = Color(0xFFFFF1E8)
private val PurpleAccent  = Color(0xFF6C4FD4)
private val PurpleLight   = Color(0xFFF0ECFF)
private val RedAccent     = Color(0xFFD93025)
private val RedLight      = Color(0xFFFFEBE9)
private val CardWhite     = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0D1B2A)
private val TextSecondary = Color(0xFF5A6A7A)
private val TextMuted     = Color(0xFF9AAABB)
private val DividerColor  = Color(0xFFE8EEF4)

// ════════════════════════════════════════════════════════════════════════════
//  DATA MODELS
//
//  API INTEGRATION NOTES:
//  ──────────────────────
//  QuickAction     → static, no API needed
//  FeaturedLawyer  → fetch from Firestore "lawyers" collection
//                    (same Lawyer model from LawyerScreen, mapped here)
//  TrendingTopic   → fetch from "trending_news" collection
//  LawTip          → fetch from "law_tips" collection or keep static
//  EmergencyContact→ static or from "emergency_contacts" collection
// ════════════════════════════════════════════════════════════════════════════

data class QuickAction(
    val id: String,
    val label: String,
    val labelBn: String,
    val icon: ImageVector,
    val accentColor: Color,
    val bgColor: Color
)

data class FeaturedLawyer(
    val id: String,
    val name: String,
    val specialization: String,
    val experience: String,
    val imageUrl: String,
    val fee: String
)

data class TrendingTopic(
    val id: String,
    val title: String,
    val tag: String,
    val tagColor: Color,
    val tagBg: Color,
    val timeAgo: String
)

data class LawTip(
    val id: String,
    val title: String,
    val body: String,
    val icon: ImageVector,
    val accentColor: Color,
    val bgColor: Color
)

data class EmergencyContact(
    val id: String,
    val label: String,
    val number: String,
    val icon: ImageVector,
    val accentColor: Color,
    val bgColor: Color
)

// ════════════════════════════════════════════════════════════════════════════
//  SAMPLE DATA  — replace with real API / Firestore data
// ════════════════════════════════════════════════════════════════════════════

val homeQuickActions = listOf(
    QuickAction("1", "Find Lawyer",   "আইনজীবী খুঁজুন", Icons.Outlined.Gavel,          BlueAccent,   BlueLight),
    QuickAction("2", "Law News",      "আইন সংবাদ",       Icons.Outlined.Newspaper,      OrangeAccent, OrangeLight),
    QuickAction("3", "Learn Law",     "আইন শিখুন",       Icons.Outlined.MenuBook,       PurpleAccent, PurpleLight),
    QuickAction("4", "File Case",     "মামলা করুন",      Icons.Outlined.Description,    GreenAccent,  GreenLight),
    QuickAction("5", "Know Rights",   "অধিকার জানুন",   Icons.Outlined.Shield,         RedAccent,    RedLight),
    QuickAction("6", "Court Info",    "আদালত তথ্য",      Icons.Outlined.AccountBalance, BlueAccent,   BlueLight),
)

val homeFeaturedLawyers = listOf(
    FeaturedLawyer("1", "Adv. Rahim Uddin",   "Criminal Law",  "12 yrs", "https://i.pravatar.cc/150?img=11", "৳ 2,000/hr"),
    FeaturedLawyer("2", "Adv. Nasrin Akter",  "Family Law",    "8 yrs",  "https://i.pravatar.cc/150?img=20", "৳ 1,500/hr"),
    FeaturedLawyer("3", "Adv. Kamal Hossain", "Corporate Law", "15 yrs", "https://i.pravatar.cc/150?img=33", "৳ 3,500/hr"),
    FeaturedLawyer("4", "Adv. Sumaiya Islam", "Civil Law",     "6 yrs",  "https://i.pravatar.cc/150?img=47", "৳ 1,200/hr"),
)

val homeTrendingTopics = listOf(
    TrendingTopic("1", "সাইবার নিরাপত্তা আইন ২০২৩ সংশোধনের প্রস্তাব",        "ট্রেন্ডিং", OrangeAccent, OrangeLight, "২ ঘণ্টা আগে"),
    TrendingTopic("2", "ভূমি নিবন্ধন আইনে বড় পরিবর্তন আসছে",                 "গুরুত্বপূর্ণ", GreenAccent, GreenLight, "৫ ঘণ্টা আগে"),
    TrendingTopic("3", "শ্রম আইন সংশোধন: গার্মেন্টস শ্রমিকদের নতুন অধিকার",  "নতুন",     BlueAccent,   BlueLight,  "১ দিন আগে"),
)

val homeLawTips = listOf(
    LawTip(
        "1",
        "FIR দায়েরের আগে জানুন",
        "থানায় FIR দায়ের করার আগে আপনার অভিযোগের প্রমাণ সংগ্রহ করুন। সাক্ষী ও ডকুমেন্ট প্রস্তুত রাখুন।",
        Icons.Outlined.Lightbulb, OrangeAccent, OrangeLight
    ),
    LawTip(
        "2",
        "ভাড়াটিয়ার আইনগত অধিকার",
        "বাড়িওয়ালা নোটিশ ছাড়া ভাড়াটিয়াকে বের করতে পারবেন না। আপনার চুক্তিপত্র সংরক্ষণ করুন।",
        Icons.Outlined.Home, PurpleAccent, PurpleLight
    ),
)

val homeEmergencyContacts = listOf(
    EmergencyContact("1", "পুলিশ",           "999",  Icons.Outlined.LocalPolice,    BlueAccent,   BlueLight),
    EmergencyContact("2", "আইনি সহায়তা",    "16430",Icons.Outlined.Gavel,          GreenAccent,  GreenLight),
    EmergencyContact("3", "জাতীয় হেল্পলাইন","333",  Icons.Outlined.Phone,          OrangeAccent, OrangeLight),
    EmergencyContact("4", "দুর্নীতি দমন",   "106",  Icons.Outlined.Security,       RedAccent,    RedLight),
)

// ════════════════════════════════════════════════════════════════════════════
//  HOME SCREEN
//
//  API INTEGRATION:
//  ────────────────
//  Replace default params with ViewModel state flows:
//    val featuredLawyers by viewModel.featuredLawyers.collectAsState()
//    val trendingTopics  by viewModel.trendingTopics.collectAsState()
//    val lawTips         by viewModel.lawTips.collectAsState()
//
//  Firestore fetch for featured lawyers (ViewModel):
//    firestore.collection("lawyers")
//        .limit(4)
//        .orderBy("createdAt", Query.Direction.DESCENDING)
//        .get()
//        .addOnSuccessListener { result ->
//            _featuredLawyers.value = result.map { doc ->
//                FeaturedLawyer(
//                    id             = doc.id,
//                    name           = doc.getString("name") ?: "",
//                    specialization = doc.getString("specialization") ?: "",
//                    experience     = doc.getString("experience") ?: "",
//                    imageUrl       = doc.getString("imageUrl") ?: "",
//                    fee            = doc.getString("fee") ?: ""
//                )
//            }
//        }
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun HomeScreen(
    userName: String                            = "স্বাগতম",
    featuredLawyers: List<FeaturedLawyer>       = homeFeaturedLawyers,
    trendingTopics: List<TrendingTopic>         = homeTrendingTopics,
    lawTips: List<LawTip>                       = homeLawTips,
    emergencyContacts: List<EmergencyContact>   = homeEmergencyContacts,
    quickActions: List<QuickAction>             = homeQuickActions,
    onQuickActionClick: (QuickAction) -> Unit   = {},
    onLawyerClick: (FeaturedLawyer) -> Unit     = {},
    onTrendingClick: (TrendingTopic) -> Unit    = {},
    onSeeAllLawyersClick: () -> Unit            = {},
    onSeeAllNewsClick: () -> Unit               = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        // ── Hero Header ─────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BlueDark, BlueAccent)))
                    .padding(horizontal = 20.dp, vertical = 26.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "EasyLaw",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "$userName 👋",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        // Notification bell
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(color = Color.White)
                                ) { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        HeroStatChip(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.Gavel,
                            value = "২৫০+",
                            label = "আইনজীবী"
                        )
                        HeroStatChip(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.MenuBook,
                            value = "১০০+",
                            label = "আইন বিষয়"
                        )
                        HeroStatChip(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.People,
                            value = "৫০০০+",
                            label = "ব্যবহারকারী"
                        )
                    }
                }
            }
        }

        // ── Quick Actions ───────────────────────────────────────────────────
        item {
            HomeSectionHeader(
                icon = Icons.Outlined.GridView,
                iconTint = BlueAccent,
                title = "দ্রুত অ্যাকশন",
                subtitle = "Quick Actions"
            )
            // 2 rows of 3 — using chunked grid
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                quickActions.chunked(3).forEach { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowItems.forEach { action ->
                            QuickActionCard(
                                action = action,
                                modifier = Modifier.weight(1f),
                                onClick = { onQuickActionClick(action) }
                            )
                        }
                    }
                }
            }
        }

        // ── Featured Lawyers ────────────────────────────────────────────────
        item {
            HomeSectionHeader(
                icon = Icons.Outlined.Gavel,
                iconTint = BlueAccent,
                title = "বিশেষজ্ঞ আইনজীবী",
                subtitle = "Featured Lawyers",
                actionLabel = "সব দেখুন",
                onActionClick = onSeeAllLawyersClick
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(featuredLawyers, key = { it.id }) { lawyer ->
                    FeaturedLawyerCard(
                        lawyer = lawyer,
                        onClick = { onLawyerClick(lawyer) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Trending News ───────────────────────────────────────────────────
        item {
            HomeSectionHeader(
                icon = Icons.Outlined.Whatshot,
                iconTint = OrangeAccent,
                title = "ট্রেন্ডিং আইন সংবাদ",
                subtitle = "Trending Legal News",
                actionLabel = "সব দেখুন",
                onActionClick = onSeeAllNewsClick
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                trendingTopics.forEach { topic ->
                    TrendingTopicCard(
                        topic = topic,
                        onClick = { onTrendingClick(topic) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Law Tips ────────────────────────────────────────────────────────
        item {
            HomeSectionHeader(
                icon = Icons.Outlined.Lightbulb,
                iconTint = OrangeAccent,
                title = "আইনি টিপস",
                subtitle = "Know Your Rights"
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                lawTips.forEach { tip ->
                    LawTipCard(tip = tip)
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Emergency Contacts ──────────────────────────────────────────────
        item {
            HomeSectionHeader(
                icon = Icons.Outlined.Emergency,
                iconTint = RedAccent,
                title = "জরুরি যোগাযোগ",
                subtitle = "Emergency Contacts"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                emergencyContacts.forEach { contact ->
                    EmergencyContactCard(contact = contact)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  HERO STAT CHIP
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun HeroStatChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.75f))
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  SECTION HEADER
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun HomeSectionHeader(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = TextMuted)
        }
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlueAccent,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = BlueAccent),
                        onClick = onActionClick
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  QUICK ACTION CARD
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun QuickActionCard(
    action: QuickAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(16.dp), ambientColor = action.accentColor.copy(0.1f))
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = action.accentColor),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(action.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    action.icon,
                    contentDescription = null,
                    tint = action.accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = action.labelBn,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  FEATURED LAWYER CARD  (horizontal scroll)
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun FeaturedLawyerCard(lawyer: FeaturedLawyer, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = BlueAccent.copy(0.12f))
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = BlueAccent),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column {
            // Top gradient bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Brush.horizontalGradient(listOf(BlueAccent, BlueDark)))
            )
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Photo
                Box(
                    modifier = Modifier
                        .size(64.dp)
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
                                .size(64.dp)
                                .clip(CircleShape),
                            error = androidx.compose.ui.res.painterResource(
                                android.R.drawable.ic_menu_gallery
                            )
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = BlueAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = lawyer.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BlueAccent.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = lawyer.specialization,
                        fontSize = 10.sp,
                        color = BlueAccent,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.WorkHistory,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(lawyer.experience, fontSize = 10.sp, color = TextSecondary)
                    }
                    Text(
                        text = lawyer.fee,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueDark
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  TRENDING TOPIC CARD
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TrendingTopicCard(topic: TrendingTopic, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = topic.tagColor.copy(0.1f))
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = topic.tagColor),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(topic.tagColor)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = topic.tagBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(topic.tagColor)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            topic.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = topic.tagColor
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = topic.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(topic.timeAgo, fontSize = 11.sp, color = TextMuted)
                }
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  LAW TIP CARD
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun LawTipCard(tip: LawTip) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = tip.accentColor.copy(0.08f)),
        color = CardWhite,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tip.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(tip.icon, contentDescription = null, tint = tip.accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(tip.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = tip.body,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  EMERGENCY CONTACT CARD
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun EmergencyContactCard(contact: EmergencyContact) {
    Surface(
        modifier = Modifier
            .width(100.dp)
            .shadow(3.dp, RoundedCornerShape(16.dp), ambientColor = contact.accentColor.copy(0.12f))
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = contact.accentColor)
            ) { /* trigger phone call intent */ },
        color = CardWhite,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(contact.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(contact.icon, contentDescription = null, tint = contact.accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = contact.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = contact.number,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contact.accentColor
            )
        }
    }
}