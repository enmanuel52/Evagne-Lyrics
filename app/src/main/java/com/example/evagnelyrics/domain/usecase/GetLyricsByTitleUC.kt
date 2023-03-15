package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import javax.inject.Inject

class GetLyricsByTitleUC @Inject constructor(
    private val lyricsRepo: LyricRepo,
){
    operator fun invoke(title: String): Lyric = lyricsRepo.getLyricByTitle(title)
}