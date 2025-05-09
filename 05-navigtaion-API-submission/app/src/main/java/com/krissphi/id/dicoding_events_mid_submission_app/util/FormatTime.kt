package com.krissphi.id.dicoding_events_mid_submission_app.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTime(rawDateTime: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
    return try {
        val date = inputFormat.parse(rawDateTime)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        rawDateTime
    }
}
