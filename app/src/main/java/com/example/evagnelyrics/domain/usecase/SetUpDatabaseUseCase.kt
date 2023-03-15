package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import javax.inject.Inject

class SetUpDatabaseUseCase @Inject constructor(
    private val insertAllLyricsUC: InsertAllLyricsUC,
    private val repo: LyricRepo,
) {

    suspend operator fun invoke(updatedBd: List<Lyric>) {
        val currentDb = repo.getAllLyrics()
        val currentTitles = currentDb.map { it.title }
        val newValues = mutableListOf<Lyric>()

        updatedBd.forEach { lyric ->
            if (!currentTitles.contains(lyric.title)) {
                newValues.add(lyric)
            }
        }

        insertAllLyricsUC(newValues)
    }
}