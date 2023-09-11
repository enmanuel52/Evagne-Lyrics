package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo

class InsertAllLyricsUC(
    private val lyricsRepo: LyricRepo,
){
    suspend operator fun invoke(lyrics: List<Lyric>) = lyricsRepo.insertAllLyrics(lyrics)
}