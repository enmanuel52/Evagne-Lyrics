package com.example.evagnelyrics.data.database.di

import androidx.room.Room
import com.example.evagnelyrics.data.database.LyricsDataBase
import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.data.repo.datasource.LyricsLocalDataSource
import com.example.evagnelyrics.domain.repo.LyricRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {
        synchronized(this) {
            Room.databaseBuilder(androidContext(), LyricsDataBase::class.java, "lyrics_table")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        }
    }

    single { get<LyricsDataBase>().getLyricsDao() }

    single <LyricsLocalDataSource>{  LyricsLocalDataSource(get())}

    single<LyricRepo> { LyricsRepoImpl(get()) }
}