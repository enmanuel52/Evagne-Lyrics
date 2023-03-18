package com.example.evagnelyrics.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.evagnelyrics.domain.usecase.*
import com.example.evagnelyrics.ui.player.Player
import com.example.evagnelyrics.ui.screen.list.ListViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
internal class ListFragmentViewModelTest {

    @RelaxedMockK
    lateinit var getAllLyricsUC: GetAllLyricsUC

    @RelaxedMockK
    lateinit var searchByTitleUC: SearchByTitleUC

    @RelaxedMockK
    lateinit var favoriteUC: FavoriteUC

    @RelaxedMockK
    lateinit var getFavoriteUC: GetFavoritesUC

    @RelaxedMockK
    lateinit var getLyricsByTitleUC: GetLyricsByTitleUC

    @RelaxedMockK
    lateinit var player: Player

    private lateinit var listFragmentViewModel: ListViewModel

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        listFragmentViewModel =
            ListViewModel(
                getAllLyricsUC,
                getFavoriteUC,
                searchByTitleUC,
                favoriteUC,
                getLyricsByTitleUC,
                player
            )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}