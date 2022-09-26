package com.example.evagnelyrics.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.FavoriteUC
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.SearchByTitleUC
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ListFragmentViewModelTest {

    @RelaxedMockK
    lateinit var getAllLyricsUC: GetAllLyricsUC

    @RelaxedMockK
    lateinit var searchByTitleUC: SearchByTitleUC

    @RelaxedMockK
    lateinit var favoriteUC: FavoriteUC

    private lateinit var listFragmentViewModel: ListFragmentViewModel

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        listFragmentViewModel = ListFragmentViewModel(getAllLyricsUC, searchByTitleUC, favoriteUC)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all titles from the use case and save in songs livedata`() = runBlocking {
        //given
        val song = LyricsEntity("Falsedad", "", false)
        listFragmentViewModel.songs.value = emptyList()
        coEvery { getAllLyricsUC() } returns listOf(song)
        //when
        listFragmentViewModel.getAllTitles()
        //then
        assert(listFragmentViewModel.songs.value == listOf(song))
    }

    @Test
    fun `set fav live data to true and filter`() {
        //given
        listFragmentViewModel.fav.value = false
        //when
        listFragmentViewModel.showFav()
        //then
        assert(listFragmentViewModel.fav.value == true)
//        verify(exactly = 1) { listFragmentViewModel.filterFav() }
    }
}