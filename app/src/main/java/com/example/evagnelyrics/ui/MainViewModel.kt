package com.example.evagnelyrics.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evagnelyrics.EvagneLyricsApp.Companion.TAG
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.domain.model.MyBox
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
) : ViewModel() {

    private val _stackState = MutableStateFlow<List<Route>>(listOf())
    val stackState get() = _stackState.asStateFlow()

    private val _myBoxState = MutableStateFlow(MyBox())
    val myBoxState get() = _myBoxState.asStateFlow()

    fun initViewModel() {
        Log.d(TAG, "init main viewModel")
        //this can be replaced by a apiCall
        val updatedDb = Items.songs.map { it.toDomain() }
        setDatabase(updatedDb)
    }

    private fun setDatabase(updatedDb: List<Lyric>) = viewModelScope.launch {
        setUpDatabaseUseCase(updatedDb)
    }

    fun addScreenToStack(screen: Route) = _stackState.update { it.plus(screen) }

    fun popScreenFromStack(screen: Route) = _stackState.update { it.minus(screen) }

    fun isOnStack(screen: Route) = _stackState.value.contains(screen)

    fun setBox(myBox: MyBox) {
        Log.d(TAG, "setBox: $myBox")
        _myBoxState.update { myBox }
    }
}