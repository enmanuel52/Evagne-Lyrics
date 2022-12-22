package com.example.evagnelyrics.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.evagnelyrics.R
import com.example.evagnelyrics.ui.navigation.AppNavigation
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_EvagneLyrics)
        super.onCreate(savedInstanceState)

        setContent {
            EvagneLyricsTheme {
                AppNavigation()
            }
        }
    }
}