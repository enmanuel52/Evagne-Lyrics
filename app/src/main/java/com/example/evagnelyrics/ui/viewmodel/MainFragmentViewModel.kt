package com.example.evagnelyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val insertAllLyricsUC: InsertAllLyricsUC,
    private val getAllLyricsUC: GetAllLyricsUC,
): ViewModel() {

    fun setDatabase() {
        if(getAllLyricsUC().isEmpty()) {
            insertAllLyricsUC(Items.songs)
        }
    }
}