package com.example.evagnelyrics.data.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.evagnelyrics.data.database.LyricsDataBase
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LyricsDaoTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var dao: LyricsDao
    private lateinit var database: LyricsDataBase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, LyricsDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getLyricsDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAllLyrics() {
        val lyrics = listOf(
            LyricsEntity("hello", ""),
            LyricsEntity("hello2", ""),
        )

        dao.insertAllLyrics(lyrics)

        val db = dao.getAllLyrics()

        assert(db == lyrics)
    }

    @Test
    fun insertAllLyrics_replaceIfEqual() {
        val lyrics = listOf(
            LyricsEntity("hello", ""),
            LyricsEntity("hello", "2"),
        )

        dao.insertAllLyrics(lyrics)

        val db = dao.getAllLyrics()

        assert(db == listOf(lyrics[1]))
    }

    @Test
    fun getLyricByTitle() {
        val lyrics = listOf(
            LyricsEntity("hello", ""),
            LyricsEntity("hello2", "2"),
        )

        dao.insertAllLyrics(lyrics)

        val lyric = dao.getLyricByTitle("hello2")

        assert(lyric == lyrics[1])
    }

    @Test
    fun searchByTitle() {
        val lyrics = listOf(
            LyricsEntity("hello", ""),
            LyricsEntity("hello2", "2"),
            LyricsEntity("helilo2", "2"),
        )

        dao.insertAllLyrics(lyrics)

        val response = dao.searchByTitle("ello")

        assert(response == lyrics.subList(0, 2))
    }
}