package com.example1.weatherapplication.weather.domain

data class Location(
    val name: String? = null,
    val region: String? = null,
    val country: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val tzId: String? = null,
    val localtimeEpoch: Int? = null,
    val localtime: String? = null
)

