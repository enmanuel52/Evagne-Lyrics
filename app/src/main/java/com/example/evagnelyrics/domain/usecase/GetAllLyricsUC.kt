package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepo
import com.example.evagnelyrics.domain.model.Lyric
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLyricsUC @Inject constructor(
    private val lyricsRepo: LyricsRepo
) {
    operator fun invoke(): Flow<List<Lyric>> = lyricsRepo.getAllLyrics()
}