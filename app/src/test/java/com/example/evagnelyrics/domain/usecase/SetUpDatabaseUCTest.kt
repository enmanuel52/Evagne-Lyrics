package com.example.evagnelyrics.domain.usecase

import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.repo.LyricRepo
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
internal class SetUpDatabaseUCTest {

    private lateinit var setUpUC: SetUpDatabaseUC

    @RelaxedMockK
    private lateinit var insertUC: InsertAllLyricsUC

    @RelaxedMockK
    private lateinit var repo: LyricRepo

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUpUC = SetUpDatabaseUC(insertUC, repo)
    }

    @Test
    fun `when the list have no different elements of the current, the newValues is an empty list`() =
        runTest {
            //given
            val lyrics = listOf(Lyric("hello", ""))
            every { repo.getAllLyrics() } returns lyrics
            //when
            setUpUC(lyrics)
            //then
            coVerify(exactly = 0) { insertUC(any()) }
        }

    @Test
    fun `when the list have  different elements of the current, the newValues won't be empty`() =
        runTest {
            //given
            val lyrics = listOf(Lyric("hello", ""), Lyric("Bye", ""))
            every { repo.getAllLyrics() } returns listOf(lyrics[1])
            //when
            setUpUC(lyrics)
            //then
            coVerify(exactly = 1) { insertUC(listOf(lyrics[0])) }
        }
}