package com.example.evagnelyrics.domain.model

data class Lyric(
    val title: String,
    val letter: String,
    var favorite: Boolean = false,
    var album: String = "Singles",
)