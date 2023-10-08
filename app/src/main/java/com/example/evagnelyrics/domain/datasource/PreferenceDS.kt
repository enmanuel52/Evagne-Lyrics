package com.example.evagnelyrics.domain.datasource

import com.example.evagnelyrics.domain.model.SystemMode
import kotlinx.coroutines.flow.Flow

interface PreferenceDS {

    suspend fun switchDarkMode()

    fun getCurrentMode(): Flow<SystemMode>
}