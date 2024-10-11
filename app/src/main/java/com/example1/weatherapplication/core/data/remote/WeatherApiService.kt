package com.example1.weatherapplication.core.data.remote

import com.example1.weatherapplication.weather.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int
    ): Response<WeatherResponse>
}