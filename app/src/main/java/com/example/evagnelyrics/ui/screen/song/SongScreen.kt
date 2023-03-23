package com.example.evagnelyrics.ui.screen.song

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.util.Horizontally
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Vertically
import com.example.evagnelyrics.ui.util.Where

@Composable
fun SongScreen(
    viewModel: SongViewModel = hiltViewModel(),
    title: String = "",
    navController: NavHostController = LocalNavController.current!!,
) {

    //observe and fetch the song
    viewModel.fetch(title)
    //here i should observe when i download the dependencies
    val song by viewModel.song.observeAsState()//= LyricsEntity("Hello", "is me")


    val visibleState = MutableTransitionState(false).apply {
        targetState = true
    }

    SlideInOutFrom(
        where = Where.Horizontal(Horizontally.End),
        visibleState = visibleState,
        durationMillis = 400,
        delayMillis = 100,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(text = song?.title.orEmpty(), style = MaterialTheme.typography.h1) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                }
            )
            SlideInOutFrom(
                where = Where.Vertical(Vertically.Bottom),
                visibleState = visibleState,
                durationMillis = 1000,
                delayMillis = 500,
            ) {
                Text(
                    text = song?.letter.orEmpty(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimen.medium)
                        .verticalScroll(rememberScrollState()),
                    color = if (MaterialTheme.colors.isLight) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground
                )
            }

        }
    }


}

@Composable
@Preview("Song screen", showSystemUi = true)
fun SongScreenPreview() {
    SongScreen(title = "Hello")
}