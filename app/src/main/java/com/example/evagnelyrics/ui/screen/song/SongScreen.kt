package com.example.evagnelyrics.ui.screen.song

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.domain.model.Lyric
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongScreen(
    title: String,
    onGoBack: () -> Unit,
) {
    val viewModel: SongViewModel = koinViewModel()
    //observe and fetch the song
    LaunchedEffect(key1 = Unit) { viewModel.fetch(title) }
    //here i should observe when i download the dependencies
    val song by viewModel.songState.collectAsState()

    SongScreen(song = song, onBack = onGoBack)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SongScreen(
    song: Lyric?,
    onBack: () -> Unit,
) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = song?.title.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        }) {
        LazyColumn(
            Modifier
                .padding(it)
                .nestedScroll(scrollBehaviour.nestedScrollConnection),
        ) {
            item {
                Text(
                    text = song?.letter.orEmpty(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimen.medium),
                )
            }
        }

    }
}

@Composable
@Preview("Song screen", showSystemUi = true)
fun SongScreenPreview() {
    SongScreen(song = Lyric("Hello", letter = "Hello, it's me")) {}
}