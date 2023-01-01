package com.example.evagnelyrics.di

import android.content.Context
import com.example.evagnelyrics.ui.player.Player
import com.example.evagnelyrics.ui.player.PlayerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlayer(@ApplicationContext context: Context): Player =
        PlayerImpl(context)
}