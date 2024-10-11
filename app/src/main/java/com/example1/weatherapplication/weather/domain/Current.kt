package com.example1.weatherapplication.weather.domain

data class Current(
    val lastUpdatedEpoch: Int? = null,
    val lastUpdated: String? = null,
    val tempC: Double? = null,
    val tempF: Double? = null,
    val isDay: Int? = null,
    val condition: Condition? = null,
    val windMph: Double? = null,
    val windKph: Double? = null,
    val windDegree: Int? = null,
    val windDir: String? = null,
    val pressureMb: Double? = null,
    val pressureIn: Double? = null,
    val precipMm: Double? = null,
    val precipIn: Double? = null,
    val humidity: Int? = null,
    val cloud: Int? = null,
    val feelslikeC: Double? = null,
    val feelslikeF: Double? = null,
    val windchillC: Double? = null,
    val windchillF: Double? = null,
    val heatindexC: Double? = null,
    val heatindexF: Double? = null,
    val dewpointC: Double? = null,
    val dewpointF: Double? = null,
    val visKm: Double? = null,
    val visMiles: Double? = null,
    val uv: Double? = null,
    val gustMph: Double? = null,
    val gustKph: Double? = null
)

