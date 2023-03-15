package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLyricsUC @Inject constructor(
    private val lyricsRepo: LyricRepo
) {
    operator fun invoke(): Flow<List<Lyric>> = lyricsRepo.getAllLyricsAsFlow()
}