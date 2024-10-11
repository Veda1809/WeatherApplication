package com.example1.weatherapplication.weather.ui

import com.example1.weatherapplication.weather.domain.Current
import com.example1.weatherapplication.weather.domain.Forecast
import com.example1.weatherapplication.weather.domain.Location
import com.example1.weatherapplication.weather.domain.Weather

data class WeatherState(
    val location: Location? = null,
    val current: Current? = null,
    val forecast: Forecast? = null,
    val isLoading: Boolean = false
)
