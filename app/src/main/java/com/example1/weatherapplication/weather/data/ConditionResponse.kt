package com.example1.weatherapplication.weather.data

import com.google.gson.annotations.SerializedName

data class ConditionResponse(
    @SerializedName("text" ) val text : String? = null,
    @SerializedName("icon" ) val icon : String? = null,
    @SerializedName("code" ) val code : Int?    = null
)
