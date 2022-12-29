package com.example.evagnelyrics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertAllLyricsUC: InsertAllLyricsUC,
    private val gellAllLyricsUC: GetAllLyricsUC
) : ViewModel() {

    private val _ready: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val ready get() = _ready.asStateFlow()

    private val allLyricsUC = gellAllLyricsUC()
    private val allLyricsState = MutableStateFlow<List<Lyric>>(emptyList())

    init {
        setDatabase()
    }

    private fun setDatabase() = viewModelScope.launch {
        allLyricsUC.collectLatest { lyrics ->
            allLyricsState.update {
                lyrics
            }
        }
        delay(1000)
        Items.songs.map { it.toDomain() }.forEach { lyric ->
            val safeTitles = allLyricsState.value.map { it.title }
            if (!safeTitles.contains(lyric.title)) {
                insertAllLyricsUC(listOf(lyric))
            }
        }


        _ready.value = true
    }
}