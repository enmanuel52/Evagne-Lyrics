package com.example.evagnelyrics.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val searchByTitleUC: SearchByTitleUC,
    private val favoriteUC: FavoriteUC,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {
    private val _songs = MutableStateFlow<List<LyricsEntity>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _fav: MutableLiveData<Boolean> = MutableLiveData(false)
    val fav: LiveData<Boolean> = _fav

    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

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

    /**
     * if it is not on fav mode*/
    fun favAction(title: String) = viewModelScope.launch{
        if (_fav.value != true) {
            val lyric = getLyricsByTitleUC(title)
            //update db
            favoriteUC(lyric)
            val ind = _songs.value.indexOf(lyric)
            //update _songs
            _songs.value = _songs.value.mapIndexed { index, lyricsEntity ->
                if (ind == index)
                    lyricsEntity.copy(favorite = !lyricsEntity.favorite)
                else
                    lyricsEntity
            }
        } else{
            //not allowed in fav mode
            _uiState.send(Resource.Error("It is not allowed on fav mode"))
        }
    }

    private fun filterFav() {
        _songs.update { songs ->
            songs.filter { it.favorite }
        }
    }

    fun onFavMode() {
        _fav.value = _fav.value != true
        if (fav.value == true) filterFav()
        else _songs.update {
            getAllLyricsUC()
        }
    }
}