package com.example.evagnelyrics.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.evagnelyrics.core.LocalActivity
import com.example.evagnelyrics.ui.navigation.AppNavigation
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplash()

        setContent {
            CompositionLocalProvider(values = arrayOf(LocalActivity provides this)) {
                EvagneLyricsTheme {
                    AppNavigation {
                        if (keepSplash) {
                            viewModel.initViewModel()
                            keepSplash = false
                        }
                    }
                }
            }
        }

    }

    /**
     * splash screen of android 12
     * */
    private fun initSplash() {
        installSplashScreen().apply {
            setKeepOnScreenCondition keep@{
                keepSplash
            }
        }
    }
}