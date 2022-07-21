package com.example.evagnelyrics.data.repo

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.datasource.LyricsLocalDataSource

class LyricsRepo(
    private val lyricsLocalDataSource: LyricsLocalDataSource,
) {
    fun insertAllLyrics(lyrics: List<LyricsEntity>) = lyricsLocalDataSource.insertAllLyrics(lyrics)

    fun getAllLyrics(): List<LyricsEntity> = lyricsLocalDataSource.getAllLyrics()

    fun getLyricByTitle(title: String): LyricsEntity = lyricsLocalDataSource.getLyricByTitle(title)

    fun searchByTitle(title: String): List<LyricsEntity> = lyricsLocalDataSource.searchByTitle(title)

    fun updateSong(lyricsEntity: LyricsEntity) = lyricsLocalDataSource.updateSong(lyricsEntity)
}