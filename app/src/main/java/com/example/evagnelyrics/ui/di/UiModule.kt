package com.example.evagnelyrics.ui.di

import com.example.evagnelyrics.data.repo.ScreenStackImpl
import com.example.evagnelyrics.domain.repo.ScreenStack
import com.example.evagnelyrics.ui.MainViewModel
import com.example.evagnelyrics.ui.screen.list.ListViewModel
import com.example.evagnelyrics.ui.screen.song.SongViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val uiModule = module {
    single<ScreenStack> { ScreenStackImpl() } withOptions {

    }
}

val vmModule = module {
    viewModelOf(::ListViewModel)
    viewModelOf(::SongViewModel)
    viewModelOf(::MainViewModel)
}