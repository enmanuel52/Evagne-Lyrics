package com.example.evagnelyrics.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.domain.model.SystemMode
import com.example.evagnelyrics.domain.usecase.GetCurrentDarkModeUC
import com.example.evagnelyrics.domain.usecase.SwitchDarkModeUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainVM(
    private val switchDarkModeUC: SwitchDarkModeUC,
    private val getCurrentDarkModeUC: GetCurrentDarkModeUC,
) : ViewModel() {


    val darkMode: Flow<SystemMode> = getCurrentDarkModeUC()

    fun switchDarkMode() = viewModelScope.launch { switchDarkModeUC() }
}