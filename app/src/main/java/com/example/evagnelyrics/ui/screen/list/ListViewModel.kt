package com.example.evagnelyrics.ui.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllLyricsUC: GetAllLyricsUC,
    private val getFavoritesUC: GetFavoritesUC,
    private val searchByTitleUC: SearchByTitleUC,
    private val favoriteUC: FavoriteUC,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
) : ViewModel() {

    private val allLyricsFlow: Flow<List<Lyric>> = getAllLyricsUC()
    private val _allLyrics: MutableStateFlow<List<Lyric>> = MutableStateFlow(emptyList())
    val allLyrics get() = _allLyrics.asStateFlow()

    val favorites: Flow<List<Lyric>> = getFavoritesUC()

    private val _favMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favMode = _favMode.asStateFlow()

    private val _titles: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val titles get() = _titles.asStateFlow()

    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

    private val _searchMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchMode get() = _searchMode.asStateFlow()

    private val _searchField: MutableLiveData<String> = MutableLiveData("")
    val searchField: LiveData<String> get() = _searchField

    private var initialized = false

    init {
        viewModelScope.launch {
            allLyricsFlow.collectLatest { lyrics ->
                _allLyrics.update {
                    lyrics
                }
                if (!initialized) {
                    initialized = true
                    //update the first time
                    _titles.update {
                        allLyrics.value.map { it.title }
                    }
                }
            }
        }
    }

    fun initValuesForTesting(
        songs: List<LyricsEntity> = emptyList(),
        fav: Boolean = false,
    ) {
//        _songs.value = songs
        _favMode.value = fav
    }

    /**
     * if it is not on fav mode*/
    fun favAction(title: String) = viewModelScope.launch {
        if (!_favMode.value) {
            val lyric: Lyric = getLyricsByTitleUC(title)
            //update db
            favoriteUC(lyric)
        } else {
            //not allowed in fav mode
            _uiState.send(Resource.Error("It is not allowed on fav mode"))
        }
    }

    fun onFavMode() = viewModelScope.launch {
        _favMode.value = _favMode.value != true
        val newTitlesFlow = if (favMode.value) favorites else allLyricsFlow
        newTitlesFlow.collectLatest { lyrics ->
            _titles.update {
                lyrics.map { it.title }
            }
        }
    }

    fun toggleSearchMode() = viewModelScope.launch {
        //when i try to turn on the search
        if (favMode.value && !searchMode.value) {
            _uiState.send(Resource.Error("It is not allowed on fav mode"))
        } else {
            _searchMode.update { !searchMode.value }
            if (!searchMode.value) {
                searching("")
            }
        }
    }

    /**
     * set the searchField to empty and shows the all list*/
    fun searching(title: String) = viewModelScope.launch {
        _searchField.value = title

        if (title.isEmpty()) {
            _titles.update {
                allLyrics.value.map { it.title }
            }
        } else {
            val lyrics = searchByTitleUC(title, fav = false)

            _titles.update {
                lyrics.map { it.title }
            }
        }
    }
}