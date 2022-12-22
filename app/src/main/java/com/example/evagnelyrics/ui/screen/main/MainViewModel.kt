package com.example.evagnelyrics.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertAllLyricsUC: InsertAllLyricsUC,
    private val getAllLyricsUC: GetAllLyricsUC,
) : ViewModel() {

    fun setDatabase() = viewModelScope.launch {
        if (getAllLyricsUC().lastOrNull()?.isEmpty() == true) {
            insertAllLyricsUC(Items.songs.map { it.toDomain() })
        }
    }
}