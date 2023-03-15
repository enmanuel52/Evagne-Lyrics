package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.repo.LyricsRepoImpl
import com.example.evagnelyrics.domain.repo.LyricRepo
import javax.inject.Inject

class EraseDatabaseUC @Inject constructor(
    private val repo: LyricRepo,
){
    suspend operator fun invoke() = repo.deleteAllLyrics()
}
