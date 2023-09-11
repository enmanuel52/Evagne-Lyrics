package com.example.evagnelyrics.domain.usecase.di

import com.example.evagnelyrics.domain.usecase.EraseDatabaseUC
import com.example.evagnelyrics.domain.usecase.FavoriteUC
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.GetFavoritesUC
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import com.example.evagnelyrics.domain.usecase.SearchByTitleUC
import com.example.evagnelyrics.domain.usecase.SetUpDatabaseUC
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ucModule = module {
    singleOf(::EraseDatabaseUC)
    singleOf(::FavoriteUC)
    singleOf(::GetAllLyricsUC)
    singleOf(::GetFavoritesUC)
    singleOf(::GetLyricsByTitleUC)
    singleOf(::InsertAllLyricsUC)
    singleOf(::SearchByTitleUC)
    singleOf(::SetUpDatabaseUC)
}