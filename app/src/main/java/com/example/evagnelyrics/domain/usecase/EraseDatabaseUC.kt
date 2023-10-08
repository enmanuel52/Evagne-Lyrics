package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.repo.LyricRepo

class EraseDatabaseUC(
    private val repo: LyricRepo,
){
    suspend operator fun invoke() = repo.deleteAllLyrics()
}
