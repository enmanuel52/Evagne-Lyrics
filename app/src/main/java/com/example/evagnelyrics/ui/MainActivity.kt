package com.example.evagnelyrics.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.evagnelyrics.core.LocalActivity
import com.example.evagnelyrics.domain.model.SystemMode
import com.example.evagnelyrics.ui.navigation.AppNavigation
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initViewModel()

        installSplashScreen()

        setContent {
            val darkMode by viewModel.darkMode.collectAsState(initial = SystemMode.Dark)

            CompositionLocalProvider(values = arrayOf(LocalActivity provides this)) {
                EvagneLyricsTheme(darkTheme = darkMode == SystemMode.Dark) {
                    Surface(color = MaterialTheme.colorScheme.background){ AppNavigation() }
                }
            }
        }

    }
}