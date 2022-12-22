package com.example.evagnelyrics

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EvagneLyricsApp : Application() {
    companion object {
        const val TAG = "Evagne Lyrics"
    }
}