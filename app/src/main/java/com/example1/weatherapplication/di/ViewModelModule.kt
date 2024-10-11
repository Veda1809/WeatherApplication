package com.example1.weatherapplication.di

import com.example1.weatherapplication.weather.ui.WeatherScreenViewModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf(::WeatherScreenViewModule)
}