package com.example1.weatherapplication.weather.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example1.weatherapplication.core.data.repository.WeatherRepository
import com.example1.weatherapplication.core.utils.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherScreenViewModule(
    private val repository: WeatherRepository
): ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    init {
        getWeatherForecast()
    }

    fun getWeatherForecast(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.getWeatherForecast(key = AppConstants.KEY, q = AppConstants.REGION, days = 10)
            state = state.copy(isLoading = false)
            result?.let {
                state = state.copy(
                    location = result.location,
                    forecast = result.forecast,
                    current = result.current
                )
            }
        }
    }
}