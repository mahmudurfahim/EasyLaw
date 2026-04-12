package com.example.easylaw.explorescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



// ════════════════════════════════════════════════════════════════════════════
//  COLORS
// ════════════════════════════════════════════════════════════════════════════
private val BgPage        = Color(0xFFF4F7FB)
private val BlueAccent    = Color(0xFF1E6FD9)
private val BlueDark      = Color(0xFF0D3E87)
private val BlueLight     = Color(0xFFE3F0FF)
private val GreenAccent   = Color(0xFF18A558)
private val GreenLight    = Color(0xFFE6F7EE)
private val OrangeAccent  = Color(0xFFE07B39)
private val OrangeLight   = Color(0xFFFFF1E8)
private val PurpleAccent  = Color(0xFF6C4FD4)
private val PurpleLight   = Color(0xFFF0ECFF)
private val CardWhite     = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0D1B2A)
private val TextSecondary = Color(0xFF5A6A7A)
private val TextMuted     = Color(0xFF9AAABB)
private val DividerColor  = Color(0xFFE8EEF4)



// ════════════════════════════════════════════════════════════════════════════
//  DATA MODELS
//
//  API INTEGRATION NOTES:
//  ─────────────────────
//  TrendingNews   → fetch from your "trending_news" Firestore collection
//  HeadlineNews   → fetch from "news" collection filtered by language field
//  LawCategory    → fetch from "law_categories" collection or keep static
//
//  All lists below are sample data. Replace with:
//    val trending by viewModel.trendingNews.collectAsState()
// ════════════════════════════════════════════════════════════════════════════

data class TrendingNews(
    val id: String,
    val tag: String,           // e.g. "সাম্প্রতিক", "গুরুত্বপূর্ণ"
    val tagColor: Color,
    val tagBg: Color,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val accentColor: Color,
    val gradientColors: List<Color>
)

data class HeadlineNews(
    val id: String,
    val title: String,
    val source: String,
    val timeAgo: String,
    val isBangla: Boolean,
    val url: String = ""          // ✅ article URL
)

data class LawLearnCategory(
    val id: String,
    val title: String,
    val titleBn: String,
    val icon: ImageVector,
    val accentColor: Color,
    val bgColor: Color,
    val articleCount: Int
)

// ════════════════════════════════════════════════════════════════════════════
//  SAMPLE DATA
// ════════════════════════════════════════════════════════════════════════════
val sampleTrending = listOf(
    TrendingNews(
        "1", "ট্রেন্ডিং", OrangeAccent, OrangeLight,
        "সাইবার নিরাপত্তা আইন ২০২৩ সংশোধনের প্রস্তাব",
        "ডিজিটাল নিরাপত্তা আইনের পরিবর্তে নতুন কাঠামো প্রস্তুত হচ্ছে",
        "২ ঘণ্টা আগে", OrangeAccent,
        listOf(Color(0xFFFF8C42), Color(0xFFE07B39))
    ),
    TrendingNews(
        "2", "গুরুত্বপূর্ণ", GreenAccent, GreenLight,
        "ভূমি নিবন্ধন আইনে বড় পরিবর্তন আসছে",
        "অনলাইনে জমি নিবন্ধনের সুযোগ তৈরি করতে সংশোধনী আনা হবে",
        "৫ ঘণ্টা আগে", GreenAccent,
        listOf(Color(0xFF22C47A), Color(0xFF18A558))
    ),
    TrendingNews(
        "3", "নতুন", BlueAccent, BlueLight,
        "শ্রম আইন সংশোধন: গার্মেন্টস শ্রমিকদের অধিকার",
        "নতুন শ্রম আইনে ছুটি ও মজুরি সংক্রান্ত বিধান পরিবর্তন হচ্ছে",
        "১ দিন আগে", BlueAccent,
        listOf(Color(0xFF3D9EFF), Color(0xFF1E6FD9))
    ),
    TrendingNews(
        "4", "বিশেষ", PurpleAccent, PurpleLight,
        "পারিবারিক আদালত: বিবাহবিচ্ছেদ মামলার নতুন নির্দেশিকা",
        "হাইকোর্টের নতুন নির্দেশনায় দ্রুত নিষ্পত্তির পথ",
        "২ দিন আগে", PurpleAccent,
        listOf(Color(0xFF9B7FE8), Color(0xFF6C4FD4))
    ),
)

val sampleEnglishHeadlines = listOf(
    HeadlineNews("e1", "Bangladesh Supreme Court Issues Landmark Ruling on Digital Rights", "The Daily Star", "3h ago", false, "https://www.thedailystar.net"),
    HeadlineNews("e2", "New Anti-Corruption Law Amendments Pass Parliament", "Dhaka Tribune", "6h ago", false, "https://www.dhakatribune.com"),
)

val sampleBanglaHeadlines = listOf(
    HeadlineNews("b1", "উচ্চ আদালতে দুর্নীতি মামলায় নতুন রায়: সম্পদ বাজেয়াপ্তের নির্দেশ", "প্রথম আলো", "৪ ঘণ্টা আগে", true, "https://www.prothomalo.com"),
    HeadlineNews("b2", "বাল্যবিবাহ রোধে আইনের কঠোর প্রয়োগের নির্দেশ দিয়েছে হাইকোর্ট", "কালের কণ্ঠ", "৮ ঘণ্টা আগে", true, "https://www.kalerkantho.com"),
)

val sampleLawCategories = listOf(
    LawLearnCategory("1", "Criminal Law",    "ফৌজদারি আইন",   Icons.Outlined.Gavel,           BlueAccent,   BlueLight,   24),
    LawLearnCategory("2", "Family Law",      "পারিবারিক আইন", Icons.Outlined.FamilyRestroom,  GreenAccent,  GreenLight,  18),
    LawLearnCategory("3", "Property Law",    "সম্পত্তি আইন",  Icons.Outlined.House,           OrangeAccent, OrangeLight, 15),
    LawLearnCategory("4", "Labour Law",      "শ্রম আইন",      Icons.Outlined.Work,            PurpleAccent, PurpleLight, 20),
    LawLearnCategory("5", "Constitutional",  "সাংবিধানিক আইন",Icons.Outlined.AccountBalance,  BlueAccent,   BlueLight,   12),
    LawLearnCategory("6", "Cyber Law",       "সাইবার আইন",    Icons.Outlined.Security,        Color(0xFF0F9BBF), Color(0xFFE0F7FA), 9),
)

// ════════════════════════════════════════════════════════════════════════════
//  SCREEN
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun ExploreScreen(
    trendingNews: List<TrendingNews>         = sampleTrending,
    englishHeadlines: List<HeadlineNews>     = emptyList(),
    banglaHeadlines: List<HeadlineNews>      = emptyList(),
    lawCategories: List<LawLearnCategory>    = sampleLawCategories,
    onTrendingClick: (TrendingNews) -> Unit  = {},
    onHeadlineClick: (HeadlineNews) -> Unit  = {},
    onLawCategoryClick: (LawLearnCategory) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        // ── Header ─────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BlueDark, BlueAccent)))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "এক্সপ্লোর",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Bangladesh Law · News & Knowledge",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
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
                }
            }
        }

        // ── Section: Trending ───────────────────────────────────────────────
        item {
            SectionHeader(
                icon = Icons.Outlined.Whatshot,
                iconTint = OrangeAccent,
                title = "ট্রেন্ডিং",
                subtitle = "আইন বিষয়ক সাম্প্রতিক আলোচনা"
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(trendingNews, key = { it.id }) { news ->
                    TrendingCard(news = news, onClick = { onTrendingClick(news) })
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Section: Headlines ──────────────────────────────────────────────
        item {
            SectionHeader(
                icon = Icons.Outlined.Newspaper,
                iconTint = BlueAccent,
                title = "সাম্প্রতিক সংবাদ",
                subtitle = if (englishHeadlines.isEmpty() && banglaHeadlines.isEmpty())
                    "Loading…" else "Recent legal headlines"
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // English headlines column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeadlineColumnHeader(label = "English", accentColor = BlueAccent)
                    englishHeadlines.forEach { news ->
                        HeadlineCard(
                            news = news,
                            accentColor = BlueAccent,
                            bgColor = BlueLight,
                            onClick = { onHeadlineClick(news) }
                        )
                    }
                }

                // Bangla headlines column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeadlineColumnHeader(label = "বাংলা", accentColor = GreenAccent)
                    banglaHeadlines.forEach { news ->
                        HeadlineCard(
                            news = news,
                            accentColor = GreenAccent,
                            bgColor = GreenLight,
                            onClick = { onHeadlineClick(news) }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Section: Learn BD Law ───────────────────────────────────────────
        item {
            SectionHeader(
                icon = Icons.Outlined.MenuBook,
                iconTint = PurpleAccent,
                title = "বাংলাদেশের আইন শিখুন",
                subtitle = "Learn Bangladeshi Law"
            )
        }

        items(
            lawCategories.chunked(2),
            key = { it.first().id }
        ) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { category ->
                    LawLearnCard(
                        category = category,
                        modifier = Modifier.weight(1f),
                        onClick = { onLawCategoryClick(category) }
                    )
                }
                // If odd number of items, fill empty space
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  SECTION HEADER
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun SectionHeader(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String
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
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = TextMuted)
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  TRENDING CARD  (horizontal scroll)
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TrendingCard(news: TrendingNews, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(260.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp), ambientColor = news.accentColor.copy(0.15f))
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = news.accentColor),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column {
            // Gradient top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Brush.horizontalGradient(news.gradientColors))
            )
            Column(modifier = Modifier.padding(14.dp)) {
                // Tag pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = news.tagBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(news.tagColor)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = news.tag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = news.tagColor
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = news.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = news.subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(news.timeAgo, fontSize = 11.sp, color = TextMuted)
                    Spacer(Modifier.weight(1f))
                    Icon(
                        Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = news.accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  HEADLINE COLUMN HEADER
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun HeadlineColumnHeader(label: String, accentColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accentColor)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  HEADLINE CARD  (two-column grid)
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun HeadlineCard(
    news: HeadlineNews,
    accentColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = accentColor.copy(0.1f))
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = accentColor),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Source pill
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = bgColor
            ) {
                Text(
                    text = news.source,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(Modifier.height(7.dp))
            // ✅ minLines = 3 fixes the height even for 1-line titles
            Text(
                text = news.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(11.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(news.timeAgo, fontSize = 10.sp, color = TextMuted)
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  LAW LEARN CARD  (2-column grid)
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun LawLearnCard(
    category: LawLearnCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = category.accentColor.copy(0.12f))
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = category.accentColor),
                onClick = onClick
            ),
        color = CardWhite,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Icon box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(category.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = category.titleBn,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = category.title,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor, thickness = 1.dp)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Article,
                    contentDescription = null,
                    tint = category.accentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${category.articleCount} articles",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = category.accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}