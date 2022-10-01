package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class FavoriteUC @Inject constructor(
    private val lyricsRepo: LyricsRepo,
){
    operator fun invoke(lyric: LyricsEntity){
        lyricsRepo.updateSong(
            lyric.copy(favorite = !lyric.favorite)
        )
    }
}