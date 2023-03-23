package com.example.evagnelyrics.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.evagnelyrics.ui.navigation.AppNavigation
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplash()

        setContent {
            EvagneLyricsTheme {
                AppNavigation { if (keepSplash) keepSplash = false }
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