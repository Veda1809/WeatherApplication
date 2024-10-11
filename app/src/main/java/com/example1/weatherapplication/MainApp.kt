package com.example1.weatherapplication

import android.app.Application
import com.example1.weatherapplication.di.appModule
import com.example1.weatherapplication.di.repositoryModule
import com.example1.weatherapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApp)
            modules(appModule, repositoryModule, viewModelModule)
        }
    }
}