package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo

class FavoriteUC(
    private val lyricsRepo: LyricRepo,
) {
    suspend operator fun invoke(lyric: Lyric) {
        lyricsRepo.updateSong(
            lyric.copy(favorite = !lyric.favorite)
        )
    }
}