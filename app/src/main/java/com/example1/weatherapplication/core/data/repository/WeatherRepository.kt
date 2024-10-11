package com.example1.weatherapplication.core.data.repository

import com.example1.weatherapplication.weather.domain.Weather

interface WeatherRepository {

    suspend fun getWeatherForecast(key:String, q: String, days: Int): Weather?
}