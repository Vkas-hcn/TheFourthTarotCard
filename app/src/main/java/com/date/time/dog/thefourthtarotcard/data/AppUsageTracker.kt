package com.date.time.dog.thefourthtarotcard.data

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppUsageTracker(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "AppUsagePrefs"
        private const val LAST_OPEN_DATE_KEY = "lastOpenDate"
    }

    fun isFirstOpenToday(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val lastOpenDate = prefs.getString(LAST_OPEN_DATE_KEY, null)
        val todayDate = dateFormat.format(Date())

        return if (todayDate != lastOpenDate) {
            prefs.edit().putString(LAST_OPEN_DATE_KEY, todayDate).apply()
            true
        } else {
            false
        }
    }
}
