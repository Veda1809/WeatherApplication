package com.example1.weatherapplication.weather.domain

data class Astro(
    val sunrise: String? = null,
    val sunset: String? = null,
    val moonrise: String? = null,
    val moonset: String? = null,
    val moonPhase: String? = null,
    val moonIllumination: Int? = null,
    val isMoonUp: Int? = null,
    val isSunUp: Int? = null
)

