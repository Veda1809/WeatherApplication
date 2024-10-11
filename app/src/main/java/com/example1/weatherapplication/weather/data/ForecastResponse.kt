package com.example1.weatherapplication.weather.data

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("forecastday" ) val forecastday : List<ForecastDayResponse>? = ArrayList()
)
