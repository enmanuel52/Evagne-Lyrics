package com.example.evagnelyrics.ui.compose.screen.song

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {

    val song: MutableLiveData<LyricsEntity?> = MutableLiveData(null)

    fun fetch(title: String) {
        song.value = getLyricsByTitleUC(title)
    }
}