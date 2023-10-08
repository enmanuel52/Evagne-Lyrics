package com.example.evagnelyrics.di

import com.example.evagnelyrics.data.database.di.dbModule
import com.example.evagnelyrics.domain.usecase.di.ucModule
import com.example.evagnelyrics.ui.di.uiModule
import com.example.evagnelyrics.ui.di.vmModule
import com.example.evagnelyrics.ui.player.Player
import com.example.evagnelyrics.ui.player.PlayerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val appModule = module {
    single<Player> { PlayerImpl(androidContext()) }

    loadKoinModules(listOf(vmModule, uiModule, dbModule, ucModule ))
}