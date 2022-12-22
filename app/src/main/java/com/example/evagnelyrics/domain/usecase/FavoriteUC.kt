package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepo
import com.example.evagnelyrics.domain.model.Lyric
import javax.inject.Inject

class FavoriteUC @Inject constructor(
    private val lyricsRepo: LyricsRepo,
) {
    suspend operator fun invoke(lyric: Lyric) {
        lyricsRepo.updateSong(
            lyric.copy(favorite = !lyric.favorite)
        )
    }
}