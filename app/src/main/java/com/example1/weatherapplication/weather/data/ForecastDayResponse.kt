package com.example1.weatherapplication.weather.data

import com.google.gson.annotations.SerializedName

data class ForecastDayResponse(
    @SerializedName("date"       ) val date      : String?         = null,
    @SerializedName("date_epoch" ) val dateEpoch : Int?            = null,
    @SerializedName("day"        ) val day       : DayResponse?    = null,
    @SerializedName("astro"      ) val astro     : AstroResponse?  = null,
    @SerializedName("hour"       ) val hour      : List<HourResponse>? = ArrayList()
)
