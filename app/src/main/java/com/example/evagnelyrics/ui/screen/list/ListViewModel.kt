package com.example.evagnelyrics.ui.screen.list

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.usecase.FavoriteUC
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.GetLyricsByTitleUC
import com.example.evagnelyrics.ui.player.Player
import com.example.evagnelyrics.ui.screen.list.model.ListFilter
import com.example.evagnelyrics.ui.screen.list.model.ListFilterEvent
import com.example.evagnelyrics.ui.screen.list.model.PlayerUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(
    getAllLyricsUC: GetAllLyricsUC,
    private val favoriteUC: FavoriteUC,
    private val getLyricsByTitleUC: GetLyricsByTitleUC,
    private val player: Player
) : ViewModel() {
    private val _uiState: Channel<Resource<Unit>> = Channel()
    val uiState get() = _uiState.receiveAsFlow()

    private val _filterState = MutableStateFlow(ListFilter())
    val filterState get() = _filterState.asStateFlow()

    val lyricsFlow: Flow<List<Lyric>> = getAllLyricsUC().combine(filterState) { lyrics, filter ->
        lyrics
            .filter { lyric -> if (filter.favorite) lyric.favorite else true }
            .filter { lyric -> lyric.title.contains(filter.searchText, ignoreCase = true) || filter.searchText.isBlank() }
    }

    private val _playerState = MutableStateFlow(PlayerUi())
    val playerState get() = _playerState.asStateFlow()

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

    /**
     * @param lyric to update the song that is playing*/
    fun onPlayer(action: PlayerAction, @RawRes rawSong: Int? = null, lyric: Lyric? = null) =
        viewModelScope.launch {
            val stopPlaying = {
                _playerState.update { it.copy(audio = Audio.Pause) }
                player.stop()
            }

            when (action) {
                PlayerAction.Play -> {
                    if (rawSong != null) {
                        player.play(rawSong, onComplete = stopPlaying)

                        _playerState.update { PlayerUi(Audio.Running, lyric) }
                    }
                }

                PlayerAction.Pause -> stopPlaying()

                PlayerAction.Clean -> player.cleanUp()
            }
        }
}

enum class PlayerAction { Play, Pause, Clean }

data class PlayerActionData(
    val action: PlayerAction,
    @RawRes val rawSong: Int? = null,
    val lyric: Lyric? = null
)