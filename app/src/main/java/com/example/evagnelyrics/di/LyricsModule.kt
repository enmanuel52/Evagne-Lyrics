package com.example.evagnelyrics.di

import android.content.Context
import androidx.room.Room
import com.example.evagnelyrics.data.database.LyricsDataBase
import com.example.evagnelyrics.data.database.dao.LyricsDao
import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.data.repo.ScreenStackImpl
import com.example.evagnelyrics.data.repo.datasource.LyricsLocalDataSource
import com.example.evagnelyrics.domain.repo.LyricRepo
import com.example.evagnelyrics.domain.repo.ScreenStack
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LyricsModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): LyricsDataBase = synchronized(this) {
        Room.databaseBuilder(context, LyricsDataBase::class.java, "lyrics_table")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideLyricsDao(lyricsDataBase: LyricsDataBase): LyricsDao = lyricsDataBase.getLyricsDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(lyricsDao: LyricsDao): LyricsLocalDataSource =
        LyricsLocalDataSource(lyricsDao)

    @Singleton
    @Provides
    fun provideRepo(lyricsLocalDataSource: LyricsLocalDataSource): LyricRepo =
        LyricsRepoImpl(lyricsLocalDataSource)

    @Singleton
    @Provides
    fun provideScreenStack(): ScreenStack = ScreenStackImpl()
}