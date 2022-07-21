package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class InsertAllLyricsUC  @Inject constructor(
    private val lyricsRepo: LyricsRepo,
){
    operator fun invoke(lyrics: List<LyricsEntity>) = lyricsRepo.insertAllLyrics(lyrics)
}