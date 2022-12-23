package com.example.evagnelyrics.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.evagnelyrics.core.Dimen
import com.example.evagnelyrics.core.LocalDimen

private val DarkColorPalette = darkColors(

)

private val LightColorPalette = lightColors(
    primary = Pink300,
    onPrimary = Color.White,
    primaryVariant = Pink300Dark,
    secondary = Purple700,
    surface = Pink300Light,
    background = Pink100,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun EvagneLyricsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(values = arrayOf(LocalDimen provides Dimen())) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                ) { content() }
            }
        )
    }
}