package com.example1.weatherapplication.di

import com.example1.weatherapplication.core.data.repository.WeatherRepository
import com.example1.weatherapplication.core.data.repository.WeatherRepositoryImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::WeatherRepositoryImp).bind<WeatherRepository>()
}