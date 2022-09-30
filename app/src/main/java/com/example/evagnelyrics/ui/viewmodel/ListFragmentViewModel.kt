package com.example.evagnelyrics.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val searchByTitleUC: SearchByTitleUC,
    private val favoriteUC: FavoriteUC,
) : ViewModel() {
    private val _songs = MutableStateFlow<List<LyricsEntity>>(emptyList())
    val songs: StateFlow<List<LyricsEntity>> get() = _songs

    private val _fav: MutableLiveData<Boolean> = MutableLiveData(false)
    val fav: LiveData<Boolean> get() = _fav

    fun initValuesForTesting(
        songs: List<LyricsEntity> = emptyList(),
        fav: Boolean = false,
    ) {
        _songs.value = songs
        _fav.value = fav
    }

    fun getAllTitles() {
        viewModelScope.launch {
            _songs.value = getAllLyricsUC()
        }
    }

    fun searchByName(title: String) {
        viewModelScope.launch {
            _songs.value = searchByTitleUC(title, fav = false)
        }
    }

    fun favAction(title: String) {
        favoriteUC(title)
        getAllTitles()
    }

    private fun filterFav() {
        _songs.value = _songs.value.filter { it.favorite }
    }

    fun onFavMode() {
        _fav.value = _fav.value != true
        if (fav.value == true) filterFav()
        else getAllTitles()
    }
}