package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepo
import javax.inject.Inject

class EraseDatabaseUC @Inject constructor(
    private val repo: LyricsRepo,
){
    suspend operator fun invoke() = repo.deleteAllLyrics()
}
