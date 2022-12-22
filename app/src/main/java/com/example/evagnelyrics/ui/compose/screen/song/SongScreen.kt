package com.example.evagnelyrics.ui.compose.screen.song

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.fragment.ACTION_BACK
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme

@Composable
fun SongScreen(
    viewModel: SongViewModel = hiltViewModel(),
    title: String,
    navTo: (route: String) -> Unit,
) {

    //observe and fetch the song
    viewModel.fetch(title)
    //here i should observe when i download the dependencies
    val song by viewModel.song.observeAsState()//= LyricsEntity("Hello", "is me")

    EvagneLyricsTheme {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(text = song?.title.orEmpty(), style = MaterialTheme.typography.h1) },
                navigationIcon = {
                    IconButton(onClick = { navTo(ACTION_BACK) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                }
            )
            Text(
                text = song?.letter.orEmpty(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimen.medium)
                    .verticalScroll(rememberScrollState()),
                color = MaterialTheme.colors.primary
            )

        }

    }

}

@Composable
@Preview("Song screen", showSystemUi = true)
fun SongScreenPreview() {
    SongScreen(title = "Hello", navTo = {})
}