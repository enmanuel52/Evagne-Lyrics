package com.example.evagnelyrics.data.repo

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.datasource.LyricsLocalDataSource
import com.example.evagnelyrics.domain.model.Lyric
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LyricsRepo(
    private val lyricsLocalDataSource: LyricsLocalDataSource,
) {
    suspend fun insertAllLyrics(lyrics: List<Lyric>) =
        lyricsLocalDataSource.insertAllLyrics(lyrics.map { LyricsEntity(it) })

    fun getAllLyrics(): Flow<List<Lyric>> =
        lyricsLocalDataSource.getAllLyrics().map { lyrics: List<LyricsEntity> ->
            lyrics.map { it.toDomain() }
        }

    fun getAllFavorites(): Flow<List<Lyric>> =
        lyricsLocalDataSource.getAllFavorites().map { lyrics: List<LyricsEntity> ->
            lyrics.map { it.toDomain() }
        }

    fun getLyricByTitle(title: String): Lyric =
        lyricsLocalDataSource.getLyricByTitle(title).toDomain()

    fun searchByTitle(title: String): List<Lyric> =
        lyricsLocalDataSource.searchByTitle(title).map { it.toDomain() }

    suspend fun updateSong(lyric: Lyric) = lyricsLocalDataSource.updateSong(LyricsEntity(lyric))
}