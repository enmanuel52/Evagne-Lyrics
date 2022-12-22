package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepo
import com.example.evagnelyrics.domain.model.Lyric
import javax.inject.Inject

class GetLyricsByTitleUC @Inject constructor(
    private val lyricsRepo: LyricsRepo,
){
    operator fun invoke(title: String): Lyric = lyricsRepo.getLyricByTitle(title)
}