package com.example.evagnelyrics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val insertAllLyricsUC: InsertAllLyricsUC,
) : ViewModel() {

    private val _ready: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val ready get() = _ready.asStateFlow()

    init {
        setDatabase()
    }

    private fun setDatabase() = viewModelScope.launch {
        if (getAllLyricsUC().lastOrNull()?.isEmpty() == true) {
            insertAllLyricsUC(Items.songs.map { it.toDomain() })
        }
        delay(1000)
        _ready.value = true
    }
}