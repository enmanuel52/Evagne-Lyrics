package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import javax.inject.Inject

class SearchByTitleUC @Inject constructor(
    private val lyricsRepo: LyricRepo,
) {
    operator fun invoke(title: String, fav: Boolean = false): List<Lyric> {
        return lyricsRepo.searchByTitle(title)
    }
}