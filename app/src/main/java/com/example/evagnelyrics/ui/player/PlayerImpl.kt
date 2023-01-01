package com.example.evagnelyrics.ui.player

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class PlayerImpl(private val context: Context) : Player {
    private var mediaPlayer: MediaPlayer? = null

    override fun play(@RawRes song: Int, onComplete: () -> Unit) {
        mediaPlayer = MediaPlayer.create(context, song).apply {
            setOnCompletionListener { onComplete() }
            start()
        }
    }

    override fun stop() {
        mediaPlayer?.stop()
    }

    override fun cleanUp() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}