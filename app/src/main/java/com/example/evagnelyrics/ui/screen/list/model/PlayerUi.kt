package com.example.evagnelyrics.ui.screen.list.model

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.ui.screen.list.Audio

data class PlayerUi(val audio: Audio = Audio.Pause, val playingSong: Lyric? = null) {
    fun isPlaying() = audio == Audio.Running

    infix fun isPlaying(lyric: Lyric) = isPlaying() && playingSong == lyric
}
