package com.example.evagnelyrics.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val searchByTitleUC: SearchByTitleUC,
    private val favoriteUC: FavoriteUC,
) : ViewModel() {
    val songs = MutableStateFlow<List<LyricsEntity>>(emptyList())
    val fav: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getAllTitles() {
        viewModelScope.launch {
            songs.value = getAllLyricsUC()
            if (fav.value!!)
                filterFav()
        }
    }

    fun searchByName(title: String) {
        viewModelScope.launch {
            songs.value = searchByTitleUC(title)
            if (fav.value!!)
                filterFav()
        }
    }

    fun favAction(title: String) {
        favoriteUC(title)
        getAllTitles()
        if (fav.value!!)
            filterFav()
    }

    private fun filterFav() {
        songs.apply {
            value = value.filter { it.favorite }
        }
    }

    fun showFav() {
        fav.apply {
            value = !(value!!)
            if (value!!) {
                //filter favs
                filterFav()
            } else {
                getAllTitles()
            }
        }
    }
}