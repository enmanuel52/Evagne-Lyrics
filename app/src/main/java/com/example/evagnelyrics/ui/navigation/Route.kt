package com.example.evagnelyrics.ui.navigation

enum class Route {
    Song, Gallery, List, Main, Picture;

    override fun toString() = when (this) {
        Song -> "Song"
        Gallery -> "Gallery"
        List -> "List"
        Main -> "Main"
        Picture -> "Picture"
    }
}