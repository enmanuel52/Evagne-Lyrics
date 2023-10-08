package com.example.evagnelyrics.ui.screen.list

import androidx.annotation.RawRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.FavoriteUC
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.GetFavoritesUC
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import com.example.evagnelyrics.ui.player.Player
import com.example.evagnelyrics.ui.screen.list.model.ListFilter
import com.example.evagnelyrics.ui.screen.list.model.ListFilterEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(
    getAllLyricsUC: GetAllLyricsUC,
    getFavoritesUC: GetFavoritesUC,
    private val favoriteUC: FavoriteUC,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
    private val player: Player
) : ViewModel() {

    val allLyricsFlow: Flow<List<Lyric>> = getAllLyricsUC()

    val favoritesFlow: Flow<List<Lyric>> = getFavoritesUC()

    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

    private val _filterState = MutableStateFlow(ListFilter())
    val filterState get() = _filterState.asStateFlow()

    private val _audioState: MutableStateFlow<Audio> = MutableStateFlow(Audio.Pause)
    val audioState get() = _audioState.asStateFlow()

    private val _playingSong: MutableState<String> = mutableStateOf("")
    val playingSong: State<String> get() = _playingSong

    fun favAction(title: String) = viewModelScope.launch {
        val lyric: Lyric = getLyricsByTitleUC(title)
        //update db
        favoriteUC(lyric)
    }

    fun onFilterEvent(event: ListFilterEvent) = viewModelScope.launch {
        when (event) {
            ListFilterEvent.Favorite -> {
                _filterState.update { it.copy(favorite = !it.favorite) }
            }

            ListFilterEvent.SearchMode -> {
                _filterState.update { it.copy(searchMode = !it.searchMode, favorite = false) }
            }

            is ListFilterEvent.SearchText -> {
                _filterState.update { it.copy(searchText = event.text) }
            }
        }
    }

    fun setAudioState(value: Audio, title: String? = null) {
        if (value == Audio.Running && title != null) {
            _playingSong.value = title
        }
        _audioState.value = value
    }

    fun onPlayer(action: PlayerAction, @RawRes song: Int? = null) =
        viewModelScope.launch {
            val stopPlaying = {
                _audioState.update { Audio.Pause }
                player.stop()
            }

            when (action) {
                PlayerAction.Play -> {
                    if (song != null) {
                        player.play(song, onComplete = stopPlaying)
                    }
                }

                PlayerAction.Pause -> stopPlaying()

                PlayerAction.Clean -> player.cleanUp()
            }
        }
}

enum class PlayerAction { Play, Pause, Clean }