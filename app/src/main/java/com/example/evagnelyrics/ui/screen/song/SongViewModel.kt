package com.example.evagnelyrics.ui.screen.song

import androidx.lifecycle.ViewModel
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SongViewModel(
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {

    private val _songState = MutableStateFlow<Lyric?>(null)
    val songState get() = _songState.asStateFlow()

    fun fetch(title: String) {
        val lyric = getLyricsByTitleUC(title)
        _songState.update { lyric }
    }
}