package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.datasource.PreferenceDS

class GetCurrentDarkModeUC(private val preferenceDS: PreferenceDS) {
    operator fun invoke() = preferenceDS.getCurrentMode()
}