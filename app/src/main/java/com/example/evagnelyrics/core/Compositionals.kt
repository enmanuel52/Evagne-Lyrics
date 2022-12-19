package com.example.evagnelyrics.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalDimen: ProvidableCompositionLocal<Dimen> = compositionLocalOf { Dimen() }

val LocalNavController: ProvidableCompositionLocal<NavHostController?> = compositionLocalOf { null }