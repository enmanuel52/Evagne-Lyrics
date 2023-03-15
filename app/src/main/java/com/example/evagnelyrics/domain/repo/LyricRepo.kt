package com.example.evagnelyrics.domain.repo

import com.example.evagnelyrics.domain.model.Lyric
import kotlinx.coroutines.flow.Flow

/**this is the repo that should be implemented*/
interface LyricRepo {

    suspend fun insertAllLyrics(lyrics: List<Lyric>)

    suspend fun deleteAllLyrics()

    fun getAllLyricsAsFlow(): Flow<List<Lyric>>

    fun getAllLyrics(): List<Lyric>

    fun getAllFavorites(): Flow<List<Lyric>>

    fun getLyricByTitle(title: String): Lyric

    fun searchByTitle(title: String): List<Lyric>

    suspend fun updateSong(lyric: Lyric)
}