package com.example.evagnelyrics.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.EvagneLyricsApp.Companion.TAG
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.model.MyBox
import com.example.evagnelyrics.domain.repo.ScreenStack
import com.example.evagnelyrics.domain.usecase.SetUpDatabaseUseCase
import com.example.evagnelyrics.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setUpDatabaseUseCase: SetUpDatabaseUseCase,
    private val screenStack: ScreenStack,
) : ViewModel() {

    private val _myBoxState = MutableStateFlow(MyBox())
    val myBoxState get() = _myBoxState.asStateFlow()

    fun initViewModel() {
        //this can be replaced by a apiCall
        val updatedDb = Items.songs.map { it.toDomain() }
        setDatabase(updatedDb)
    }

    private fun setDatabase(updatedDb: List<Lyric>) = viewModelScope.launch {
        setUpDatabaseUseCase(updatedDb)
    }

    fun pushScreen(screen: Route) = screenStack.push(screen)

    fun popScreen() = screenStack.pop()

    fun previous() = screenStack.previous

    fun setBox(myBox: MyBox) {
        Log.d(TAG, "setBox: $myBox")
        _myBoxState.update { myBox }
    }
}