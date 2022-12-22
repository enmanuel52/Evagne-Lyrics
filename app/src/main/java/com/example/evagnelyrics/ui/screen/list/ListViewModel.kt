package com.example.evagnelyrics.ui.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.usecase.FavoriteUC
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import com.example.evagnelyrics.domain.usecase.SearchByTitleUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val searchByTitleUC: SearchByTitleUC,
    private val favoriteUC: FavoriteUC,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {

    private val _favMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favMode = _favMode.asStateFlow()

    private val _favs: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val favs get() = _favs.asStateFlow()

    private val _titles: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val titles get() = _titles.asStateFlow()

    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

    private val _searchMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchMode get() = _searchMode.asStateFlow()

    private val _searchField: MutableLiveData<String> = MutableLiveData("")
    val searchField: LiveData<String> get() = _searchField

    fun initValuesForTesting(
        songs: List<LyricsEntity> = emptyList(),
        fav: Boolean = false,
    ) {
//        _songs.value = songs
        _favMode.value = fav
    }

    init {
        getAllSongs()
    }

    fun getAllSongs() {
        viewModelScope.launch {
            val lyrics = getAllLyricsUC()

            _titles.update {
                lyrics.map { it.title }
            }

            _favs.update {
                lyrics.filter { it.favorite }.map { it.title }
            }
        }
    }

    /**
     * if it is not on fav mode*/
    fun favAction(title: String) = viewModelScope.launch {
        if (!_favMode.value) {
            val lyric = getLyricsByTitleUC(title)
            //update db
            favoriteUC(lyric)
            favsUpdate(lyric.title)
        } else {
            //not allowed in fav mode
            _uiState.send(Resource.Error("It is not allowed on fav mode"))
        }
    }

    private fun favsUpdate(title: String) {
        _favs.update {
            if (it.contains(title)) {
                it.minus(title)
            } else {
                it.plus(title)
            }
        }

    }

    private fun filterFav() {
        _titles.update { list ->
            list.filter { favs.value.contains(it) }
        }
    }

    fun onFavMode() {
        _favMode.value = _favMode.value != true
        if (favMode.value) filterFav()
        else getAllSongs()
    }

    fun toggleSearchMode() = viewModelScope.launch {
        _searchMode.update { !searchMode.value }
    }

    fun searching(title: String) {
        _searchField.value = title

        val lyrics = searchByTitleUC(title, fav = false)

        _titles.update {
            lyrics.map { it.title }
        }
    }
}