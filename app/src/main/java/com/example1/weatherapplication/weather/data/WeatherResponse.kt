package com.example1.weatherapplication.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location" ) val location : LocationResponse? = null,
    @SerializedName("current"  ) val current  : CurrentResponse?  = null,
    @SerializedName("forecast" ) val forecast : ForecastResponse? = null
)
