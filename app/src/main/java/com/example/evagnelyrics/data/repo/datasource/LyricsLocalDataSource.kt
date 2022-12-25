package com.example.evagnelyrics.data.repo.datasource

import com.example.evagnelyrics.data.database.dao.LyricsDao
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import kotlinx.coroutines.flow.MutableStateFlow

class LyricsLocalDataSource(
    private val lyricsDao: LyricsDao,
) {
    suspend fun insertAllLyrics(lyrics: List<LyricsEntity>) = lyricsDao.insertAllLyrics(lyrics)

    fun getAllLyrics() = lyricsDao.getAllLyrics()

    fun getLyricByTitle(title: String): LyricsEntity = lyricsDao.getLyricByTitle(title)

    fun searchByTitle(title: String): List<LyricsEntity> = lyricsDao.searchByTitle(title)

    suspend fun updateSong(lyricsEntity: LyricsEntity) = lyricsDao.updateSong(lyricsEntity)

    fun getAllFavorites() = lyricsDao.getAllFavorites()

    fun deleteAllLyrics() = lyricsDao.deleteAll()
}