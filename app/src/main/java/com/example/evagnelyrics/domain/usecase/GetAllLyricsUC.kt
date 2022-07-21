package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class GetAllLyricsUC @Inject constructor(
    private val lyricsRepo: LyricsRepo
) {
    operator fun invoke() = lyricsRepo.getAllLyrics()
}