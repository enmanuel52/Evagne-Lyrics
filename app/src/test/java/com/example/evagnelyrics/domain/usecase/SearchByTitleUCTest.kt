package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.data.repo.LyricsRepo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SearchByTitleUCTest {

    @RelaxedMockK
    private lateinit var lyricsRepo: LyricsRepo

    private lateinit var searchByTitleUC: SearchByTitleUC

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchByTitleUC = SearchByTitleUC(lyricsRepo)
    }

    @Test
    fun `returns only favorites`() {
        val lyric1 = LyricsEntity("1", "", false)
        val lyric2 = LyricsEntity("21", "", true)
        //Given
        every { lyricsRepo.searchByTitle("1") } returns listOf(lyric1, lyric2)
        //When
        val result = searchByTitleUC("1", true)
        //Then
        assert(result == listOf(lyric2))
    }

    @Test
    fun `returns all if not favorite on`() {
        val lyric1 = LyricsEntity("1", "", false)
        val lyric2 = LyricsEntity("21", "", false)
        //Given
        every { lyricsRepo.searchByTitle("1") } returns listOf(lyric1, lyric2)
        //When
        val result = searchByTitleUC("1")
        //Then
        assert(result == listOf(lyric1, lyric2))
    }
}