package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import kotlinx.coroutines.flow.Flow

class GetFavoritesUC(
    private val repo: LyricRepo,
) {
    operator fun invoke(): Flow<List<Lyric>> = repo.getAllFavorites()
}
