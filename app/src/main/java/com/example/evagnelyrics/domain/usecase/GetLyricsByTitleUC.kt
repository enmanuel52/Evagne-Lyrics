package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo

class GetLyricsByTitleUC(
    private val lyricsRepo: LyricRepo,
){
    operator fun invoke(title: String): Lyric = lyricsRepo.getLyricByTitle(title)
}