package com.example1.weatherapplication.weather.mapper

import com.example1.weatherapplication.weather.data.AstroResponse
import com.example1.weatherapplication.weather.data.ConditionResponse
import com.example1.weatherapplication.weather.data.CurrentResponse
import com.example1.weatherapplication.weather.data.DayResponse
import com.example1.weatherapplication.weather.data.ForecastDayResponse
import com.example1.weatherapplication.weather.data.ForecastResponse
import com.example1.weatherapplication.weather.data.HourResponse
import com.example1.weatherapplication.weather.data.LocationResponse
import com.example1.weatherapplication.weather.data.WeatherResponse
import com.example1.weatherapplication.weather.domain.Astro
import com.example1.weatherapplication.weather.domain.Condition
import com.example1.weatherapplication.weather.domain.Current
import com.example1.weatherapplication.weather.domain.Day
import com.example1.weatherapplication.weather.domain.Forecast
import com.example1.weatherapplication.weather.domain.ForecastDay
import com.example1.weatherapplication.weather.domain.Hour
import com.example1.weatherapplication.weather.domain.Location
import com.example1.weatherapplication.weather.domain.Weather

fun WeatherResponse.toWeather(): Weather{
    return Weather(
        location = location?.toLocation(),
        current = current?.toCurrent(),
        forecast = forecast?.toForecast()
    )
}

fun LocationResponse.toLocation(): Location{
    return Location(
        name = name ?: "",
        region = region ?: "",
        country = country ?: "",
        lat = lat ?: 0.0,
        lon = lon ?: 0.0,
        tzId = tzId ?: "",
        localtimeEpoch = localtimeEpoch ?: 0,
        localtime = localtime ?: ""
    )
}

fun CurrentResponse.toCurrent(): Current{
    return Current(
        lastUpdatedEpoch = lastUpdatedEpoch ?: 0,
        lastUpdated = lastUpdated ?: "",
        tempC = tempC ?: 0.0,
        tempF = tempF ?: 0.0,
        isDay = isDay ?: 0,
        windDir = windDir ?: "",
        windKph = windKph ?: 0.0,
        windMph = windMph ?: 0.0,
        windchillC = windchillC ?: 0.0,
        windchillF = windchillF ?: 0.0,
        windDegree = windDegree ?: 0,
        precipIn = precipIn ?: 0.0,
        precipMm = precipMm ?: 0.0,
        pressureIn = pressureIn ?: 0.0,
        pressureMb = pressureMb ?: 0.0,
        dewpointC = dewpointC ?: 0.0,
        dewpointF = dewpointF ?: 0.0,
        gustKph = gustKph ?: 0.0,
        gustMph = gustMph ?: 0.0,
        heatindexC = heatindexC ?: 0.0,
        heatindexF = heatindexF ?: 0.0,
        feelslikeC = feelslikeC ?: 0.0,
        feelslikeF = feelslikeF ?: 0.0,
        humidity = humidity ?: 0,
        cloud = cloud ?: 0,
        visKm = visKm ?: 0.0,
        visMiles = visMiles ?: 0.0,
        uv = uv ?: 0.0,
        condition = condition?.toCondition()
    )
}

fun ConditionResponse.toCondition(): Condition{
    return Condition(
        text = text ?: "",
        icon = icon ?: "",
        code = code ?: 0
    )
}

fun ForecastResponse.toForecast(): Forecast{
    return Forecast(
        forecastday = forecastday?.map { it.toForecastDay() }
    )
}

fun ForecastDayResponse.toForecastDay(): ForecastDay{
    return ForecastDay(
        day = day?.toDay(),
        dateEpoch = dateEpoch ?: 0,
        date = date ?: "",
        astro = astro?.toAstro(),
        hour = hour?.map { it.toHour() }
    )
}

fun AstroResponse.toAstro(): Astro{
    return Astro(
        sunrise = sunrise ?: "",
        sunset = sunset ?: "",
        moonrise = moonrise ?: "",
        moonset = moonset ?: "",
        moonPhase = moonPhase ?: "",
        moonIllumination = moonIllumination ?: 0,
        isSunUp = isSunUp ?: 0,
        isMoonUp = isMoonUp ?: 0
    )
}

fun HourResponse.toHour(): Hour{
    return Hour(
        timeEpoch = timeEpoch ?: 0,
        time = time ?: "",
        tempF = tempF ?: 0.0,
        tempC = tempC ?: 0.0,
        isDay = isDay ?: 0,
        condition = condition?.toCondition(),
        windDir = windDir ?: "",
        windKph = windKph ?: 0.0,
        windMph = windMph ?: 0.0,
        windchillC = windchillC ?: 0.0,
        windchillF = windchillF ?: 0.0,
        windDegree = windDegree ?: 0,
        precipIn = precipIn ?: 0.0,
        precipMm = precipMm ?: 0.0,
        pressureIn = pressureIn ?: 0.0,
        pressureMb = pressureMb ?: 0.0,
        gustMph = gustMph ?: 0.0,
        gustKph = gustKph ?: 0.0,
        willItRain = willItRain ?: 0,
        willItSnow = willItSnow ?: 0,
        dewpointF = dewpointF ?: 0.0,
        dewpointC = dewpointC ?: 0.0,
        feelslikeC = feelslikeC ?: 0.0,
        feelslikeF = feelslikeF ?: 0.0,
        snowCm = snowCm ?: 0.0,
        visKm = visKm ?: 0.0,
        cloud = cloud ?: 0,
        chanceOfRain = chanceOfRain ?: 0,
        chanceOfSnow = chanceOfSnow ?: 0,
        humidity = humidity ?: 0,
        heatindexC = heatindexC ?: 0.0,
        heatindexF = heatindexF ?: 0.0,
        visMiles = visMiles ?: 0.0,
        uv = uv ?: 0.0
    )
}

fun DayResponse.toDay(): Day{
    return Day(
        maxtempC = maxtempC ?: 0.0,
        maxtempF = maxtempF ?: 0.0,
        mintempC = mintempC ?: 0.0,
        mintempF = mintempF ?: 0.0,
        avgtempC = avgtempC ?: 0.0,
        avgtempF = avgtempF ?: 0.0,
        maxwindKph = maxwindKph ?: 0.0,
        maxwindMph = maxwindMph ?: 0.0,
        totalprecipIn = totalprecipIn ?: 0.0,
        totalprecipMm = totalprecipMm ?: 0.0,
        totalsnowCm = totalsnowCm ?: 0.0,
        avgvisKm = avgvisKm ?: 0.0,
        avgvisMiles = avgvisMiles ?: 0.0,
        avghumidity = avghumidity ?: 0,
        dailyChanceOfRain = dailyChanceOfRain ?: 0,
        dailyChanceOfSnow = dailyChanceOfSnow ?: 0,
        dailyWillItRain = dailyWillItRain ?: 0,
        dailyWillItSnow = dailyWillItSnow ?: 0,
        condition = condition?.toCondition(),
        uv = uv ?: 0.0,
    )
}

