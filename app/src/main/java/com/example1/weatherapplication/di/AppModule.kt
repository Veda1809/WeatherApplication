package com.example1.weatherapplication.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example1.weatherapplication.core.data.remote.WeatherApiService
import com.example1.weatherapplication.core.utils.RetrofitUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(ChuckerInterceptor(androidContext()))
        client.addInterceptor(logging)
        client.build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        RetrofitUtils.createApiService<WeatherApiService>(get())
    }
}