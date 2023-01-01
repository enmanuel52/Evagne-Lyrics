package com.example.evagnelyrics.ui.player

import androidx.annotation.RawRes

interface Player {

    fun play(@RawRes song: Int, onComplete: () -> Unit)

    fun stop()

    fun cleanUp()
}