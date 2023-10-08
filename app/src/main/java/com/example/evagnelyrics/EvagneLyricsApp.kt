package com.example.evagnelyrics

import android.app.Application
import com.example.evagnelyrics.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EvagneLyricsApp : Application() {
    companion object {
        const val TAG = "EvagneLyricsCommon"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EvagneLyricsApp)
            modules(appModule)
        }
    }
}