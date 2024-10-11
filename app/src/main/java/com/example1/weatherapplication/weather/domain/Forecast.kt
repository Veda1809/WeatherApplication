package com.example1.weatherapplication.weather.domain

data class Forecast(
    val forecastday : List<ForecastDay>? = ArrayList()
)
