package com.example1.weatherapplication.weather.domain

data class Weather(
    val location : Location? = null,
    val current  : Current?  = null,
    val forecast : Forecast? = null
)
