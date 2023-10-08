package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo

class SearchByTitleUC(
    private val lyricsRepo: LyricRepo,
) {
    operator fun invoke(title: String, fav: Boolean = false): List<Lyric> {
        return lyricsRepo.searchByTitle(title)
    }
}