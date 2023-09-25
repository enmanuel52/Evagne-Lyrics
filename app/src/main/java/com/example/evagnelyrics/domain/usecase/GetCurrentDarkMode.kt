package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.datasource.PreferenceDS

class GetCurrentDarkMode(private val preferenceDS: PreferenceDS) {
    operator fun invoke() = preferenceDS.getCurrentMode()
}