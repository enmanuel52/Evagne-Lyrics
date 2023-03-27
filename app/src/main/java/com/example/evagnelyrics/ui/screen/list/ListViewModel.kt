package com.example.evagnelyrics.ui.screen.list

import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.EvagneLyricsApp.Companion.TAG
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.*
import com.example.evagnelyrics.ui.player.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val player: Player
) : ViewModel() {

    private val allLyricsFlow: Flow<List<Lyric>> = getAllLyricsUC()
    private val _allLyrics: MutableStateFlow<List<Lyric>> = MutableStateFlow(emptyList())
    private val allLyrics get() = _allLyrics.asStateFlow()

    val favoritesFlow: Flow<List<Lyric>> = getFavoritesUC()
    private val _favorites: MutableStateFlow<List<Lyric>> = MutableStateFlow(emptyList())
    private val favorites get() = _favorites.asStateFlow()

    //I just wanna use a State
    private val _favState: MutableState<Boolean> = mutableStateOf(false)
    val favState: State<Boolean> get() = _favState

    private val _audioState: MutableStateFlow<Audio> = MutableStateFlow(Audio.Pause)
    val audioState get() = _audioState.asStateFlow()

    private val _titles: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val titles get() = _titles.asStateFlow()

    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

    private val _searchMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchMode get() = _searchMode.asStateFlow()

    private val _searchField: MutableLiveData<String> = MutableLiveData("")
    val searchField: LiveData<String> get() = _searchField

    private val _playingSong: MutableState<String> = mutableStateOf("")
    val playingSong: State<String> get() = _playingSong

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
                    try{ _playingSong.value = lyrics.first().title }catch (e: NoSuchElementException){
                        Log.d(TAG, "empty list")
                    }
                }
            }
        }
        viewModelScope.launch {
            favoritesFlow.collectLatest { lyrics ->
                _favorites.update {
                    lyrics
                }
            }
        }
    }

    fun initValuesForTesting(
        songs: List<LyricsEntity> = emptyList(),
        fav: Boolean = false,
    ) {
//        _songs.value = songs
//        _favMode.value = fav
    }

    /**
     * if it is not on fav mode*/
    fun favAction(title: String) = viewModelScope.launch {
        if (!_favState.value) {
            val lyric: Lyric = getLyricsByTitleUC(title)
            //update db
            favoriteUC(lyric)
        } else {
            //not allowed in fav mode
            _uiState.send(Resource.Error("It is not allowed on fav mode"))
        }
    }

    fun onFavMode() = viewModelScope.launch {
        if (audioState.value == Audio.Running) {
            _uiState.send(Resource.Error("It is not allowed when you're playing the song"))
            return@launch
        }

        _favState.value = _favState.value != true

        _titles.update {
            if (favState.value) {
                favorites.value.map { it.title }
            } else {
                allLyrics.value.map { it.title }
            }
        }
    }

    fun toggleSearchMode() = viewModelScope.launch {
        //when i try to turn on the search
        if (favState.value && !searchMode.value) {
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

    fun setAudioState(value: Audio, title: String? = null) {
        if (value == Audio.Running && title != null) {
            _playingSong.value = title
        }
        _audioState.value = value
    }

    fun onPlayer(action: PlayerAction, @RawRes song: Int? = null, onComplete: () -> Unit = {}) = viewModelScope.launch{
        when (action) {
            PlayerAction.Play -> {
                if (song != null) {
                    player.play(song, onComplete)
                }
            }
            PlayerAction.Pause -> {
                _audioState.value = Audio.Pause
                player.stop()
            }
            PlayerAction.Clean -> player.cleanUp()
        }
    }
}

enum class PlayerAction { Play, Pause, Clean }