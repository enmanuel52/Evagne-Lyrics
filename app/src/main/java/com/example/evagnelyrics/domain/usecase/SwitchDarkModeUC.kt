package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.datasource.PreferenceDS

class SwitchDarkModeUC(
    private val preferenceDS: PreferenceDS,
){
    suspend operator fun invoke() = preferenceDS.switchDarkMode()
}