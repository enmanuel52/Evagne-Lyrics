package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import javax.inject.Inject

class InsertAllLyricsUC  @Inject constructor(
    private val lyricsRepo: LyricRepo,
){
    suspend operator fun invoke(lyrics: List<Lyric>) = lyricsRepo.insertAllLyrics(lyrics)
}