package com.example1.weatherapplication.weather.domain

data class ForecastDay(
    val date: String? = null,
    val dateEpoch: Int? = null,
    val day: Day? = null,
    val astro: Astro? = null,
    val hour: List<Hour>? = ArrayList()
)
