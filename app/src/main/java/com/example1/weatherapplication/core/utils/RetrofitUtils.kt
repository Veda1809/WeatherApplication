package com.example1.weatherapplication.core.utils

import retrofit2.Retrofit

object RetrofitUtils {
    inline fun <reified T> createApiService(retrofit: Retrofit): T{
        return retrofit.create(T::class.java)
    }
}