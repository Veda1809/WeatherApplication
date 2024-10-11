package com.example1.weatherapplication.core.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatDateFromString(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMMM", Locale.getDefault())

        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid date format"
        }
    }



    fun fixApiUrl(apiUrl: String): String {

        val fullUrl = buildString {
            append("https:")
            append(apiUrl)
        }

        return fullUrl

    }

    fun getDayOfWeek(dateString: String?): String {
        if (dateString.isNullOrBlank()) {
            return "Date not available"
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(dateString) ?: throw ParseException("Invalid date", 0)

            val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            outputFormat.format(date) // Returns the day of the week
        } catch (e: ParseException) {
            throw IllegalArgumentException("Unparseable date: $dateString", e)
        }
    }

    fun calculateDaylightHours(sunrise: String, sunset: String): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

        val sunriseTime = LocalTime.parse(sunrise.trim(), formatter)
        val sunsetTime = LocalTime.parse(sunset.trim(), formatter)

        val daylightDuration = Duration.between(sunriseTime, sunsetTime)

        val hours = daylightDuration.toHours().toInt()
        val minutes = (daylightDuration.toMinutes() % 60).toInt()

        return "${hours}h ${minutes}min"
    }


}