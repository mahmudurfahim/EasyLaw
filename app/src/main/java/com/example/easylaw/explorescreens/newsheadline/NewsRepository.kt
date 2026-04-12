package com.example.easylaw.explorescreens.newsheadline

import com.example.easylaw.explorescreens.HeadlineNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class NewsRepository {

    suspend fun fetchEnglishHeadlines(limit: Int = 5): List<HeadlineNews> =
        fetchFeed(
            url      = FeedConfig.ENGLISH_FEED_URL,
            source   = FeedConfig.ENGLISH_SOURCE,
            isBangla = false,
            prefix   = "e",
            limit    = limit
        )

    suspend fun fetchBanglaHeadlines(limit: Int = 5): List<HeadlineNews> =
        fetchFeed(
            url      = FeedConfig.BANGLA_FEED_URL,
            source   = FeedConfig.BANGLA_SOURCE,
            isBangla = true,
            prefix   = "b",
            limit    = limit
        )

    private suspend fun fetchFeed(
        url: String,
        source: String,
        isBangla: Boolean,
        prefix: String,
        limit: Int
    ): List<HeadlineNews> = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection().apply {
                connectTimeout = 8_000
                readTimeout    = 8_000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }
            val items = connection.getInputStream().use { RssParser.parse(it) }

            items.take(limit).mapIndexed { index, item ->
                HeadlineNews(
                    id       = "${prefix}${index + 1}",
                    title    = item.title,
                    source   = source,
                    timeAgo  = RssParser.toRelativeTime(item.pubDate),
                    isBangla = isBangla,
                    url      = item.link      // ✅ article URL mapped
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}