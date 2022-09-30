package com.example.evagnelyrics.data.database.dao

import androidx.room.*
import com.example.evagnelyrics.data.database.entities.LYRICS_TABLE_NAME
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface LyricsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllLyrics(lyrics: List<LyricsEntity>)

    @Query("SELECT * FROM $LYRICS_TABLE_NAME ORDER BY title")
    fun getAllLyrics(): List<LyricsEntity>

    @Query("SELECT * FROM $LYRICS_TABLE_NAME WHERE title LIKE :title")
    fun getLyricByTitle(title: String): LyricsEntity

    @Query("SELECT * FROM $LYRICS_TABLE_NAME WHERE title LIKE '%' || :title || '%' ORDER BY title")
    fun searchByTitle(title: String): List<LyricsEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSong(lyricsEntity: LyricsEntity)
}