package com.example.evagnelyrics.core

import android.app.Activity
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.example.evagnelyrics.ui.MainActivity

val LocalDimen: ProvidableCompositionLocal<Dimen> = compositionLocalOf { Dimen() }

val LocalActivity: ProvidableCompositionLocal<MainActivity?> = compositionLocalOf { null }