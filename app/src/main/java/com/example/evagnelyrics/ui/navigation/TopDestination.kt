package com.example.evagnelyrics.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.evagnelyrics.R

enum class TopDestination(@StringRes val title: Int,@DrawableRes val picture: Int) {
    Songs(R.string.songs, R.drawable.img2_7190),
    Gallery(R.string.title_wallpapers,R.drawable.img4_7236)
}