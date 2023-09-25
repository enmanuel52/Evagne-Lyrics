package com.example.evagnelyrics.data.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.evagnelyrics.domain.datasource.PreferenceDS
import com.example.evagnelyrics.domain.model.SystemMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PreferenceDSImpl constructor(private val context: Context) : PreferenceDS {

    val Context.dataStore by preferencesDataStore("preferences")

    private object Keys {
        val SYSTEM_MODE = stringPreferencesKey("system_mode")
    }

    override suspend fun switchDarkMode() {
        val current = context.dataStore.data.map { it[Keys.SYSTEM_MODE] }.firstOrNull()
            ?: SystemMode.Dark.toString()

        val newValue = when (SystemMode.valueOf(current)) {
            SystemMode.Dark,
            SystemMode.System -> SystemMode.Light

            SystemMode.Light -> SystemMode.Dark
        }
        context.dataStore.edit {
            it[Keys.SYSTEM_MODE] = newValue.toString()
        }
    }

    override fun getCurrentMode(): Flow<SystemMode> = context.dataStore.data.map {
        it[Keys.SYSTEM_MODE]?.let { string -> SystemMode.valueOf(string) } ?: SystemMode.Dark
    }
}