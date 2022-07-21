package com.example.evagnelyrics.data.repo.datasource

import com.example.evagnelyrics.data.database.dao.LyricsDao
import com.example.evagnelyrics.data.database.entities.LyricsEntity

class LyricsLocalDataSource(
    private val lyricsDao: LyricsDao,
) {
    fun insertAllLyrics(lyrics: List<LyricsEntity>) = lyricsDao.insertAllLyrics(lyrics)

    fun getAllLyrics(): List<LyricsEntity> = lyricsDao.getAllLyrics()

    fun getLyricByTitle(title: String): LyricsEntity = lyricsDao.getLyricByTitle(title)

    fun searchByTitle(title: String): List<LyricsEntity> = lyricsDao.searchByTitle(title)

    fun updateSong(lyricsEntity: LyricsEntity) = lyricsDao.updateSong(lyricsEntity)
}