package com.example.evagnelyrics.core

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val MaterialTheme.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current

val Colors.primaryVariantPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isLight) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary