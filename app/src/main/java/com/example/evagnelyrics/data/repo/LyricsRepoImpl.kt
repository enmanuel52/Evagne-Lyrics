package com.example.evagnelyrics.data.repo

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.datasource.LyricsLocalDataSource
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LyricsRepoImpl(
    private val lyricsLocalDataSource: LyricsLocalDataSource,
) : LyricRepo {
    override suspend fun insertAllLyrics(lyrics: List<Lyric>) =
        lyricsLocalDataSource.insertAllLyrics(lyrics.map { LyricsEntity(it) })

    override fun getAllLyrics(): List<Lyric> =
        lyricsLocalDataSource.getAllLyrics().map { it.toDomain() }

    override suspend fun deleteAllLyrics() =
        lyricsLocalDataSource.deleteAllLyrics()

    override fun getAllLyricsAsFlow(): Flow<List<Lyric>> =
        lyricsLocalDataSource.getAllLyricsAsFlow().map { lyrics: List<LyricsEntity> ->
            lyrics.map { it.toDomain() }
        }

    override fun getAllFavorites(): Flow<List<Lyric>> =
        lyricsLocalDataSource.getAllFavorites().map { lyrics: List<LyricsEntity> ->
            lyrics.map { it.toDomain() }
        }

    override fun getLyricByTitle(title: String): Lyric =
        lyricsLocalDataSource.getLyricByTitle(title).toDomain()

    override fun searchByTitle(title: String): List<Lyric> =
        lyricsLocalDataSource.searchByTitle(title).map { it.toDomain() }

    override suspend fun updateSong(lyric: Lyric) =
        lyricsLocalDataSource.updateSong(LyricsEntity(lyric))
}