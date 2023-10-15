package com.example.evagnelyrics

import android.app.Application
import com.example.evagnelyrics.di.appModule
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}