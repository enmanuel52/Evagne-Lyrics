package com.example.evagnelyrics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.SetUpDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setUpDatabaseUseCase: SetUpDatabaseUseCase,
) : ViewModel() {

    private val _ready: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val ready get() = _ready.asStateFlow()

    init {
        //this can be replaced by a apiCall
        val updatedDb = Items.songs.map { it.toDomain() }
        setDatabase(updatedDb)
    }

    private fun setDatabase(updatedDb: List<Lyric>) = viewModelScope.launch {
        setUpDatabaseUseCase(updatedDb)
        delay(1000)

        _ready.value = true
    }
}