package com.example.evagnelyrics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.model.SystemMode
import com.example.evagnelyrics.domain.usecase.GetCurrentDarkModeUC
import com.example.evagnelyrics.domain.usecase.SetUpDatabaseUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel (
    private val setUpDatabaseUC: SetUpDatabaseUC,
    private val getCurrentDarkModeUC: GetCurrentDarkModeUC,
) : ViewModel() {

    val darkMode: Flow<SystemMode> = getCurrentDarkModeUC()

    fun initViewModel() {
        //this can be replaced by a apiCall
        val updatedDb = Items.songs.map { it.toDomain() }
        setDatabase(updatedDb)
    }

    private fun setDatabase(updatedDb: List<Lyric>) = viewModelScope.launch {
        setUpDatabaseUC(updatedDb)
    }
}