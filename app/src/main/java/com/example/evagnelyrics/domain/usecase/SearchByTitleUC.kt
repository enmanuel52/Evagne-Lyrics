package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class SearchByTitleUC @Inject constructor(
    private val lyricsRepo: LyricsRepo,
) {
    operator fun invoke(title: String, fav: Boolean = false): List<LyricsEntity> {
        return lyricsRepo.searchByTitle(title)
//        return if (fav)
//            result.filter { it.favorite }
//        else
//            result
    }
}