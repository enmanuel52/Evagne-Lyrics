package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class FavoriteUC @Inject constructor(
    private val lyricsRepo: LyricsRepo,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
){
    operator fun invoke(title: String): Unit{
        val lyric = getLyricsByTitleUC(title)
        lyricsRepo.updateSong(
            LyricsEntity(
                title,
                letter= lyric.letter,
                favorite = !(lyric.favorite)
            )
        )
    }
}