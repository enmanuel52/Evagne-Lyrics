package com.example.evagnelyrics.ui.screen.main

import androidx.lifecycle.ViewModel
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.domain.usecase.GetAllLyricsUC
import com.example.evagnelyrics.domain.usecase.InsertAllLyricsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertAllLyricsUC: InsertAllLyricsUC,
    private val getAllLyricsUC: GetAllLyricsUC,
) : ViewModel() {

//    val nightMode: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setDatabase() {
        if (getAllLyricsUC().isEmpty()) {
            insertAllLyricsUC(Items.songs)
        }
    }

//    fun changeLightMode() {
//        nightMode.value = !(nightMode.value!!)
//    }
}