package com.example.evagnelyrics.domain.model

data class Lyric(
    val title: String,
    val letter: String,
    val favorite: Boolean = false,
    val album: String = "Singles",
)