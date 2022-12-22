package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import com.example.evagnelyrics.domain.model.Lyric
import javax.inject.Inject

class InsertAllLyricsUC  @Inject constructor(
    private val lyricsRepo: LyricsRepo,
){
    suspend operator fun invoke(lyrics: List<Lyric>) = lyricsRepo.insertAllLyrics(lyrics)
}