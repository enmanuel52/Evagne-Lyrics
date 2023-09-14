package com.example.evagnelyrics.ui.di

import com.example.evagnelyrics.data.repo.ScreenStackImpl
import com.example.evagnelyrics.domain.repo.ScreenStack
import com.example.evagnelyrics.ui.MainViewModel
import com.example.evagnelyrics.ui.screen.list.ListViewModel
import com.example.evagnelyrics.ui.screen.song.SongViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    single <ScreenStack>{ ScreenStackImpl()  }
}

val vmModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::SongViewModel)
    factoryOf(::MainViewModel)
}