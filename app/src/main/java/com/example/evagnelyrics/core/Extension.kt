package com.example.evagnelyrics.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

typealias Material3 = androidx.compose.material3.MaterialTheme

val Material3.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current
