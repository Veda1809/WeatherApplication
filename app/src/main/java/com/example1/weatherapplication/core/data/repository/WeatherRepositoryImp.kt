package com.example1.weatherapplication.core.data.repository

import com.example1.weatherapplication.core.data.remote.WeatherApiService
import com.example1.weatherapplication.weather.domain.Weather
import com.example1.weatherapplication.weather.mapper.toWeather

class WeatherRepositoryImp (
    private val apiService: WeatherApiService
): WeatherRepository {
    override suspend fun getWeatherForecast(key: String, q: String, days: Int): Weather? {
        val response = apiService.getWeatherForecast(key = key, q = q, days = days)
        return if(response.isSuccessful){
            response.body()?.toWeather()
        } else {
            null
        }
    }
}