package com.example.easylaw.explorescreens.newsheadline

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

data class RssItem(
    val title: String,
    val pubDate: String,
    val link: String
)

object RssParser {

    private val rssDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)

    fun parse(inputStream: InputStream): List<RssItem> {
        val items = mutableListOf<RssItem>()
        val parser = Xml.newPullParser().apply {
            setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            setInput(inputStream, null)
        }

        var title   = ""
        var pubDate = ""
        var link    = ""
        var inItem  = false
        var tag     = ""

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    tag = parser.name ?: ""
                    if (tag == "item") inItem = true
                }
                XmlPullParser.TEXT -> {
                    if (inItem) when (tag) {
                        "title"   -> title   += parser.text ?: ""
                        "pubDate" -> pubDate += parser.text ?: ""
                        "link"    -> link    += parser.text ?: ""
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "item" && inItem) {
                        items.add(RssItem(title.trim(), pubDate.trim(), link.trim()))
                        title = ""; pubDate = ""; link = ""; inItem = false
                    }
                    tag = ""
                }
            }
            eventType = parser.next()
        }
        return items
    }

    fun toRelativeTime(pubDate: String): String {
        return try {
            val date = rssDateFormat.parse(pubDate) ?: return pubDate
            val diffMs  = System.currentTimeMillis() - date.time
            val diffMin = (diffMs / 60_000).coerceAtLeast(1)   // ✅ minimum 1m ago
            when {
                diffMin < 60   -> "${diffMin}m ago"
                diffMin < 1440 -> "${diffMin / 60}h ago"
                else           -> "${diffMin / 1440}d ago"
            }
        } catch (e: Exception) { pubDate }
    }
}