package com.example.evagnelyrics.ui.screen.song

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {

    val song: MutableLiveData<Lyric> = MutableLiveData(null)

    fun fetch(title: String) {
        song.value = getLyricsByTitleUC(title)
    }
}